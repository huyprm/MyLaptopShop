package com.ptithcm2021.laptopshop.model.dto.request;

import com.ptithcm2021.laptopshop.validator.PhoneNumberConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Email(message = "Email is not correct format")
    private String email;

    @PhoneNumberConstraint
    private String phoneNumber;

    @NotBlank(message = "Birthday cannot be blank")
    private String dob;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;
}
