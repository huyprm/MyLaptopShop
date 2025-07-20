package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.ChangeUserRoleRequest;
import com.ptithcm2021.laptopshop.model.dto.request.CreateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.response.UserResponse;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import jakarta.mail.MessagingException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    void initAdminUser();

    void createUser(CreateUserRequest createUserRequest) throws MessagingException;

    UserResponse updateUser(UpdateUserRequest updateUserRequest);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    UserResponse changeUserRole(ChangeUserRoleRequest request);

    UserResponse fetchInfoUser();

    void changPassword(String oldPassword, String newPassword);

    void updateAvatar(MultipartFile avatar) throws IOException;

    void changeEmail(String newEmail, String otpCode);

    void sendOTPChangeEmail(String email) throws MessagingException;

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    void deleteUser();
}
