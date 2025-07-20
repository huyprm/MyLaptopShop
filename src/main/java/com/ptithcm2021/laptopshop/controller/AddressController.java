package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.AddressRequest;
import com.ptithcm2021.laptopshop.model.dto.response.AddressResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.service.AddressService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/create")
    public ApiResponse<AddressResponse > createAddress(@RequestBody AddressRequest request) {
        return ApiResponse.<AddressResponse>builder().data(addressService.addAddress(request)).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressResponse> updateAddress(@PathVariable Long id, @RequestBody AddressRequest request) {
        return ApiResponse.<AddressResponse>builder().data(addressService.updateAddress(request, id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ApiResponse.<Void>builder().message("Deleted successful").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AddressResponse> getAddress(@PathVariable Long id) {
        return ApiResponse.<AddressResponse>builder().data(addressService.getAddress(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<AddressResponse>> getAllAddresses() {
        return ApiResponse.<List<AddressResponse>>builder().data(addressService.getListAddress()).build();
    }
}
