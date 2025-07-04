package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.UserMapper;
import com.ptithcm2021.laptopshop.model.dto.request.ChangeUserRoleRequest;
import com.ptithcm2021.laptopshop.model.dto.request.CreateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.response.UserResponse;
import com.ptithcm2021.laptopshop.model.entity.LoginIdentifier;
import com.ptithcm2021.laptopshop.model.entity.Role;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import com.ptithcm2021.laptopshop.repository.LoginIdentifierRepository;
import com.ptithcm2021.laptopshop.repository.RoleRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.FileService;
import com.ptithcm2021.laptopshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
        String otpCache = Objects.requireNonNull(redisTemplate.opsForValue().get("otp:" + request.getUsername())).toString();

        if(request.getOtpCode().equals(otpCache)){
            redisTemplate.delete("otp:" + request.getUsername());
        } else {
            throw new AppException(ErrorCode.OTP_INCORRECT);
        }

        Role role = roleRepository.findById("CUSTOMER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        User user= User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Collections.singleton(role))
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
    public UserResponse updateUser(String userId, UpdateUserRequest updateUserRequest) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, updateUserRequest);
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
    public UserResponse fetchInfoUser(String accessToken) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    @Override
    public void changPassword(String oldPassword, String newPassword, String userId) {
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
    public void updateAvatar(MultipartFile avatar, String userId) throws IOException {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String url = fileService.uploadFile(avatar, user.getAvatar());
        user.setAvatar(url);
        userRepository.save(user);
    }
}
