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

    @PreAuthorize("#userId == authentication.name ")
    UserResponse updateUser(String userId, UpdateUserRequest updateUserRequest);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    UserResponse changeUserRole(ChangeUserRoleRequest request);

    UserResponse fetchInfoUser();

    @PreAuthorize("hasAuthority(#userId == authentication.name)")
    void changPassword(String oldPassword, String newPassword, String userId);

    @PreAuthorize("#userId == authentication.name ")
    void updateAvatar(MultipartFile avatar, String userId) throws IOException;

    @PreAuthorize("#userId == authentication.name or hasAuthority('SCOPE_OWNER')")
    void deleteUser(String userId);
}
