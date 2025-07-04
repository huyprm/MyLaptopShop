package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.ChangeUserRoleRequest;
import com.ptithcm2021.laptopshop.model.dto.request.CreateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateUserRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.UserResponse;
import com.ptithcm2021.laptopshop.service.UserService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse<String> createUser(@RequestBody @Valid CreateUserRequest request) throws MessagingException {
        userService.createUser(request);
        return ApiResponse.<String>builder().message("Create user successful").build();
    }

    @PostMapping("/{userId}/update")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UpdateUserRequest request){
        return ApiResponse.<UserResponse>builder().data(userService.updateUser(userId, request)).build();
    }

    @PostMapping("/change-role")
    public ApiResponse<UserResponse> changeUserRole(@RequestBody @Valid ChangeUserRoleRequest request){
        return ApiResponse.<UserResponse>builder().data(userService.changeUserRole(request)).build();
    }

    @PostMapping("/{userId}/change-password")
    public ApiResponse<String> changePassword(@RequestParam String oldPassword,
                                              @RequestParam String newPassword,
                                              @PathVariable String userId){
        userService.changPassword(oldPassword, newPassword, userId);
        return ApiResponse.<String>builder().message("Change password successful").build();
    }

    @PostMapping("/{userId}/update-avatar")
    public ApiResponse<String> updateUserAvatar(@PathVariable String userId, @RequestBody MultipartFile file) throws IOException {
        userService.updateAvatar(file, userId);
        return ApiResponse.<String>builder().message("Update user avatar successful").build();
    }

}
