package com.ptithcm2021.laptopshop.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ptithcm2021.laptopshop.model.entity.Role;
import com.ptithcm2021.laptopshop.validator.PhoneNumberConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dob;
    private String gender;
    @JsonIgnoreProperties("permissions")
    private Set<Role> roles;
    private String avatar;
    private List<AddressResponse> address;
    private int amountUsed;
    private boolean blocked;
}
