package com.ptithcm2021.laptopshop.service;

import com.google.api.services.drive.Drive;
import com.ptithcm2021.laptopshop.model.dto.request.CartRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CartResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
public interface CartService {

    CartResponse addCart(CartRequest cartRequest);

    CartResponse updateCart(CartRequest cartRequest);

    void removeCart(long productDetailId);

    CartResponse getCart(long productDetailId);

    List<CartResponse> getCartList();
}
