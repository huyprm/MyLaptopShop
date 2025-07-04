package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.ChangeUserRoleRequest;
import com.ptithcm2021.laptopshop.model.dto.request.CreateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.response.UserResponse;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import jakarta.mail.MessagingException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    void initAdminUser();

    void createUser(CreateUserRequest createUserRequest) throws MessagingException;

    @PostAuthorize("#userId == authentication.name ")
    UserResponse updateUser(String userId, UpdateUserRequest updateUserRequest);

    @PostAuthorize("hasAuthority('FULL_CONTROL')")
    UserResponse changeUserRole(ChangeUserRoleRequest request);

    UserResponse fetchInfoUser(String accessToken);

    @PostAuthorize("hasAuthority(#userId == authentication.name)")
    void changPassword(String oldPassword, String newPassword, String userId);

    @PostAuthorize("#userId == authentication.name ")
    void updateAvatar(MultipartFile avatar, String userId) throws IOException;
}
