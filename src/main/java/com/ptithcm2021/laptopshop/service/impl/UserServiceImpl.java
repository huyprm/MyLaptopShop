package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.UserMapper;
import com.ptithcm2021.laptopshop.model.dto.request.ChangeUserRoleRequest;
import com.ptithcm2021.laptopshop.model.dto.request.CreateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.UserResponse;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.AddressService;
import com.ptithcm2021.laptopshop.service.FileService;
import com.ptithcm2021.laptopshop.service.UserService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import com.ptithcm2021.laptopshop.util.TemplateEmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoginIdentifierRepository loginIdentifierRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RoleRepository roleRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final FileService fileService;
    private final AddressService addressService;
    private final RankLevelRepository rankLevelRepository;
    private final MailServiceImpl mailService;

    @Override
    public void initAdminUser() {
        if (loginIdentifierRepository.existsByIdentifierValue("admin")) {
            log.info("Admin user already exists, skipping init.");
            return;
        }

        Role adminRole = roleRepository.findById("ADMIN")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = User.builder()
                .roles(Collections.singleton(adminRole))
                .build();

        LoginIdentifier loginIdentifier = LoginIdentifier.builder()
                .identifierType(LoginTypeEnum.USERNAME)
                .identifierValue("admin")
                .secret(passwordEncoder.encode("admin"))
                .user(user)
                .build();

        loginIdentifierRepository.save(loginIdentifier);
        log.info("Default admin user created: username=admin, password=admin");
    }

    @Override
    public void createUser(CreateUserRequest request){
        String otpCache = Objects.requireNonNull(redisTemplate.opsForValue().get("otp-create:" + request.getUsername())).toString();

        if(request.getOtpCode().equals(otpCache)){
            redisTemplate.delete("otp-create:" + request.getUsername());
        } else {
            throw new AppException(ErrorCode.OTP_INCORRECT);
        }

        Role role = roleRepository.findById("CUSTOMER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        RankLevel defaultRank = rankLevelRepository.findByPriorityAndActive(1, true);


        User user= User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Collections.singleton(role))
                .email(request.getUsername())
                .currentRankLevel(defaultRank)
                .build();

        LoginIdentifier loginIdentifier = LoginIdentifier.builder()
                .identifierType(LoginTypeEnum.EMAIL)
                .identifierValue(request.getUsername())
                .secret(passwordEncoder.encode(request.getPassword()))
                .user(user)
                .build();

        loginIdentifierRepository.save(loginIdentifier);
    }



    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        String userId = FetchUserIdUtil.fetchUserId();
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, updateUserRequest);

        if (updateUserRequest.getAddressRequests() != null && !updateUserRequest.getAddressRequests().isEmpty()) {
            user.getAddress().addAll(addressService.addListAddress(updateUserRequest.getAddressRequests(), user));
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse changeUserRole(ChangeUserRoleRequest request) {
        User user =  userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Set<Role> roles = new HashSet<>();
        request.getRoleIds().forEach(roleId -> {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() ->  new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(role);
        });

        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse fetchInfoUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    @Override
    public void changPassword(String oldPassword, String newPassword) {
        String userId = FetchUserIdUtil.fetchUserId();
        LoginIdentifier loginIdentifier = loginIdentifierRepository.findByIdentifierTypeAndUserId(LoginTypeEnum.EMAIL, userId)
                .orElseThrow(() ->  new AppException(ErrorCode.IDENTIFIER_UNDETERMINED));

        if(passwordEncoder.matches(oldPassword, loginIdentifier.getSecret())){
            loginIdentifier.setSecret(passwordEncoder.encode(newPassword));
            loginIdentifierRepository.save(loginIdentifier);
        }else {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
    }

    @Override
    public void updateAvatar(MultipartFile avatar) throws IOException {
        String userId = FetchUserIdUtil.fetchUserId();
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String url = fileService.uploadFile(avatar, user.getAvatar());
        user.setAvatar(url);
        userRepository.save(user);
    }

    @Override
    public void changeEmail(String newEmail, String otpCode) {
        String userId = FetchUserIdUtil.fetchUserId();

        String key = "otp-change-email:" + userId;
        String otpCache = Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(Object::toString)
                .orElseThrow(() -> new AppException(ErrorCode.OTP_EXPIRED));

        if (!otpCode.equals(otpCache)) {
            throw new AppException(ErrorCode.OTP_INCORRECT);
        }

        redisTemplate.delete("otp-change-email:" + newEmail);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void sendOTPChangeEmail(String email) throws MessagingException {
        String userId = FetchUserIdUtil.fetchUserId();
        if (!loginIdentifierRepository.existsByIdentifierValue(email)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        int otp = 100000 + new Random().nextInt(900000);
        redisTemplate.opsForValue().set("otp-change-email:" + userId, otp, Duration.ofMinutes(5));

        mailService.sendMimeEmail(email, "Xác thực email", TemplateEmailUtil.subjectMailSendOTP(String.valueOf(otp)));
    }

    @Override
    public void deleteUser() {
        String userId = FetchUserIdUtil.fetchUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setBlocked(true);
        user.getLoginIdentifiers().clear();
        log.info(String.valueOf(user.getLoginIdentifiers().getClass()));
        userRepository.save(user);
    }

    @Override
    public PageWrapper<UserResponse> getAllUsers(int page, int size, String keyword, String roleId, boolean blocked) {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = userRepository.findAll(
                UserSpecification.filter(
                        keyword,
                        roleId,
                        blocked
                ),
                pageable);

        return PageWrapper.<UserResponse>builder()
                .content(users.stream().map(userMapper::toUserResponse).collect(Collectors.toList()))
                .pageNumber(users.getNumber())
                .pageSize(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }
}
