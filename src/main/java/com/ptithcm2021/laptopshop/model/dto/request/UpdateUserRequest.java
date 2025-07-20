package com.ptithcm2021.laptopshop.model.dto.request;

import com.ptithcm2021.laptopshop.validator.DOBConstraint;
import com.ptithcm2021.laptopshop.validator.PhoneNumberConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @PhoneNumberConstraint
    private String phoneNumber;

    @DOBConstraint()
    private String dob;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    private List<AddressRequest> addressRequests;
}
