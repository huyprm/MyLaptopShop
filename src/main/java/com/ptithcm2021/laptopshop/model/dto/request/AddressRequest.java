package com.ptithcm2021.laptopshop.model.dto.request;

import com.ptithcm2021.laptopshop.validator.PhoneNumberConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String address;
    @PhoneNumberConstraint()
    private String phone;
    private String recipient;
    private boolean isDefault; // true if this is the default address
}
