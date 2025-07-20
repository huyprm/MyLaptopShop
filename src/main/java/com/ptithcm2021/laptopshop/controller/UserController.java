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
import lombok.Getter;
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

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UpdateUserRequest request){
        return ApiResponse.<UserResponse>builder().data(userService.updateUser(request)).build();
    }

    @PutMapping("/change-role")
    public ApiResponse<UserResponse> changeUserRole(@RequestBody @Valid ChangeUserRoleRequest request){
        return ApiResponse.<UserResponse>builder().data(userService.changeUserRole(request)).build();
    }

    @PutMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestParam String oldPassword,
                                              @RequestParam String newPassword){
        userService.changPassword(oldPassword, newPassword);
        return ApiResponse.<String>builder().message("Change password successful").build();
    }

    @PutMapping("/update-avatar")
    public ApiResponse<String> updateUserAvatar(@RequestBody MultipartFile file) throws IOException {
        userService.updateAvatar(file);
        return ApiResponse.<String>builder().message("Update user avatar successful").build();
    }

    @GetMapping("/fetchInfo")
    public ApiResponse<UserResponse> fetchUserInfo(){
        return  ApiResponse.<UserResponse>builder().data(userService.fetchInfoUser()).build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteUser(){
        userService.deleteUser();
        return ApiResponse.<Void>builder().message("Delete user successful").build();
    }

    @PutMapping("/change-email")
    public ApiResponse<String> changeEmail(@RequestParam String newEmail,
                                           @RequestParam String otpCode) {
        userService.changeEmail(newEmail, otpCode);
        return ApiResponse.<String>builder().message("Change email successful").build();
    }

    @GetMapping("/send-otp-change-email")
    public ApiResponse<String> sendOtpChangeEmail(@RequestParam String email) throws MessagingException {
        userService.sendOTPChangeEmail(email);
        return ApiResponse.<String>builder().message("Send OTP successful").build();
    }
}
