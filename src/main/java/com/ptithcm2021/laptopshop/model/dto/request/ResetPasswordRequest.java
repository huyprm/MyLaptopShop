package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email()
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String newPassword;

    @NotBlank(message = "Reset token cannot be blank")
    private String resetToken;
}
