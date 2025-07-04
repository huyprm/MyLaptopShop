package com.ptithcm2021.laptopshop.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.dto.request.LoginRequest;
import com.ptithcm2021.laptopshop.model.dto.request.ResetPasswordRequest;
import com.ptithcm2021.laptopshop.model.entity.LoginIdentifier;
import com.ptithcm2021.laptopshop.model.entity.Permission;
import com.ptithcm2021.laptopshop.model.entity.Role;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import com.ptithcm2021.laptopshop.repository.LoginIdentifierRepository;
import com.ptithcm2021.laptopshop.repository.RoleRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.AuthenticationService;
import com.ptithcm2021.laptopshop.util.TemplateEmailUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final LoginIdentifierRepository loginIdentifierRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MailServiceImpl mailService;
    private final RoleRepository roleRepository;

    @Value("${jwt.signer_key}")
    protected String SIGNER_KEY;

    @Value("${jwt.access_expiration_time}")
    protected int JWT_ACCESS_TIME;

    @Value("${jwt.refresh_expiration_time}")
    protected int JWT_REFRESH_TIME;

    @Override
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws KeyLengthException {
        String refreshToken = revokeRefreshToken(request);

        String userId = verifyRefreshToken(refreshToken);
        if (userId != null){
            User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            String accessToken = generateAccessToken(user);
            String newRefreshToken = generateRefreshToken(user);

            addRefreshTokenToCookie(response, newRefreshToken, JWT_REFRESH_TIME);
            saveRefreshTokenToRedis(newRefreshToken, user.getId());
            return accessToken;
        }else {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public boolean verifyAccessToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        boolean verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return true;
    }

    @Override
    public String login(LoginRequest request, HttpServletResponse response) throws KeyLengthException {
        LoginIdentifier loginIdentifier = loginIdentifierRepository.findByIdentifierValue(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), loginIdentifier.getSecret())){
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        User user = loginIdentifier.getUser();
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        addRefreshTokenToCookie(response, refreshToken, JWT_REFRESH_TIME);
        saveRefreshTokenToRedis(refreshToken, user.getId());
        return accessToken;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        revokeRefreshToken(request);
        addRefreshTokenToCookie(response, "", 0);
    }

    @Override
    public String verifyOTP(String email, String otp) {
        String otpCache = Optional.ofNullable(redisTemplate.opsForValue().get("otp:" + email))
                .map(Object::toString)
                .orElseThrow(() -> new AppException(ErrorCode.REVOKED_TOKEN));

        if(otp.equals(otpCache)){
            redisTemplate.delete("otp_forgot_pw:" + email);
        } else {
            throw new AppException(ErrorCode.OTP_INCORRECT);
        }
        String restToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("reset_token:"+ email, restToken, Duration.ofMinutes(10));
        return restToken;
    }

    @Override
    public void sendOTPViaMail(String email) throws MessagingException {
        if(loginIdentifierRepository.existsByIdentifierValue(email)){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        redisTemplate.opsForValue().set("otp:"+email, otp, Duration.ofMinutes(5));

        mailService.sendMimeEmail(email, "Xác thực email", TemplateEmailUtil.subjectMailSendOTP(String.valueOf(otp)));
    }

    @Override
    public void sendOTPForgotPw(String email) throws MessagingException {
        if(!loginIdentifierRepository.existsByIdentifierValue(email)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        redisTemplate.opsForValue().set("otp_forgot_pw:"+email, otp, Duration.ofMinutes(5));

        mailService.sendMimeEmail(email, "Xác thực email", TemplateEmailUtil.subjectMailSendOTP(String.valueOf(otp)));
    }
    @Override
    public String loginWithIdentifier(String mail, LoginTypeEnum loginType, String firstName, String lastName, HttpServletResponse response) throws KeyLengthException {
        LoginIdentifier loginIdentifier = loginIdentifierRepository.findByIdentifierValue(mail)
                .orElse(null);

        if(loginIdentifier == null){
            loginIdentifier = createUserWithIdentifier(mail, loginType, firstName, lastName);
        }

        User user = loginIdentifier.getUser();
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        addRefreshTokenToCookie(response, refreshToken, JWT_REFRESH_TIME);
        saveRefreshTokenToRedis(refreshToken, user.getId());
        return accessToken;
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String tokenCache = Optional.ofNullable(redisTemplate.opsForValue().get("reset_token:" + request.getEmail()))
                .map(Object::toString)
                .orElseThrow(() -> new AppException(ErrorCode.REVOKED_TOKEN));

        if(tokenCache.equals(request.getResetToken())){
            LoginIdentifier loginIdentifier = loginIdentifierRepository.findByIdentifierValue(request.getEmail())
                    .orElseThrow(() -> new AppException(ErrorCode.IDENTIFIER_UNDETERMINED));

            loginIdentifier.setSecret(passwordEncoder.encode(request.getNewPassword()));
            loginIdentifierRepository.save(loginIdentifier);
        } else {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private String generateAccessToken(User user) throws KeyLengthException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512).type(JOSEObjectType.JWT).build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("ptithcm2021.com")
                .jwtID(UUID.randomUUID().toString())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(JWT_ACCESS_TIME, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", fetchPermission(user))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    private String generateRefreshToken(User user) {
        return UUID.randomUUID().toString();
    }


    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken, int TTL) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setMaxAge( TTL * 24 * 60 * 60);
        cookie.setPath("/api/auth");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    private void saveRefreshTokenToRedis(String refreshToken, String userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("revoked", "false");
        map.put("refresh_token", refreshToken);
        map.put("issuedAt", String.valueOf(System.currentTimeMillis()));

        redisTemplate.opsForHash().putAll("refresh_token:" + refreshToken, map);
        redisTemplate.expire("refresh_token:" + refreshToken, JWT_REFRESH_TIME, TimeUnit.DAYS);
    }

    private String verifyRefreshToken(String refreshToken) {
        String key = "refresh_token:" + refreshToken;
        if (!redisTemplate.hasKey(key)) {
            log.debug("Refresh token {} không tồn tại", refreshToken);
            return null;
        }
        Object revoked = redisTemplate.opsForHash().get(key, "revoked");
        if ("true".equals(revoked)) {
            log.debug("Refresh token {} đã bị revoke", refreshToken);
            return null;
        }

        return Optional.ofNullable(redisTemplate.opsForHash().get(key, "userId"))
                .map(Object::toString)
                .orElseThrow(() -> new AppException(ErrorCode.REVOKED_TOKEN));
    }

    private String revokeRefreshToken(HttpServletRequest request) {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        redisTemplate.opsForHash().put("refresh_token:" + refreshToken, "revoked", "true");
        return refreshToken;
    }

    private Set<String> fetchPermission(User user){
        Set<String> permissions = new HashSet<>();
        user.getRoles().forEach(role -> {
            permissions.addAll(role.getPermission().stream().map(Permission::getId).collect(Collectors.toSet()));
        });
        return permissions;
    }

    private LoginIdentifier createUserWithIdentifier(String mail, LoginTypeEnum loginType, String lastName, String firstName) {
        Role role = roleRepository.findById("CUSTOMER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        User user= User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .roles(Collections.singleton(role))
                .build();

        LoginIdentifier loginIdentifier = LoginIdentifier.builder()
                .identifierType(loginType)
                .identifierValue(mail)

                .user(user)
                .build();

        return loginIdentifierRepository.save(loginIdentifier);
    }
//    private String checkUsernameType(String username){
//        if(username.matches("^0\\d{9}$")){
//            return LoginTypeEnum.PHONE.name();
//        }else if(username.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
//            return LoginTypeEnum.EMAIL.name();
//        }
//        return LoginTypeEnum.USERNAME.name();
//    }
}
