package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.AddressRequest;
import com.ptithcm2021.laptopshop.model.dto.response.AddressResponse;
import com.ptithcm2021.laptopshop.model.entity.Address;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_USER')")
public interface AddressService {
    AddressResponse getAddress(Long id);
    List<AddressResponse> getListAddress();
    AddressResponse addAddress(AddressRequest request);

    List<Address> addListAddress(List<AddressRequest> request, User user);

    AddressResponse updateAddress(AddressRequest request, Long id);

    void setDefaultAddress(Long id);

    void deleteAddress(Long id);

}
