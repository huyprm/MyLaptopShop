package com.ptithcm2021.laptopshop.service;

import com.google.api.services.drive.Drive;
import com.ptithcm2021.laptopshop.model.dto.request.CartRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CartResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CartService {
    @PreAuthorize("#cartRequest.userId == authentication.name and hasAuthority('SCOPE_USER')")
    CartResponse addCart(CartRequest cartRequest);

    @PreAuthorize("#cartRequest.userId == authentication.name and hasAuthority('SCOPE_USER')")
    CartResponse updateCart(CartRequest cartRequest);

    @PreAuthorize("#userId == authentication.name and hasAuthority('SCOPE_USER')")
    void removeCart(long productDetailId, String userId);

    @PreAuthorize("#userId == authentication.name and hasAuthority('SCOPE_USER')")
    CartResponse getCart(long productDetailId, String userId);

    @PreAuthorize("#userId == authentication.name and hasAuthority('SCOPE_USER')")
    List<CartResponse> getCartList(String userId);
}
