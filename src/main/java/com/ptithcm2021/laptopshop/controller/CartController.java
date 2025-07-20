package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.CartRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.CartResponse;
import com.ptithcm2021.laptopshop.model.entity.Cart;
import com.ptithcm2021.laptopshop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/create")
    public ApiResponse<CartResponse> createCart(@RequestBody @Valid CartRequest request) {
        return ApiResponse.<CartResponse>builder().data(cartService.addCart(request)).build();
    }

    @PutMapping("/update")
    public ApiResponse<CartResponse> updateCart(@RequestBody @Valid CartRequest request) {
        return ApiResponse.<CartResponse>builder().data(cartService.updateCart(request)).build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteCart(@RequestParam long productDetailId) {
        cartService.removeCart(productDetailId);
        return ApiResponse.<Void>builder().message("Deleted cart successful").build();
    }

//    @GetMapping("/cart")
//    public ApiResponse<CartResponse> getCart(@RequestParam long productDetailId) {
//        return ApiResponse.<CartResponse>builder().data(cartService.getCart(productDetailId)).build();
//    }

    @GetMapping("")
    public ApiResponse<List<CartResponse>> getAllCart() {
        return ApiResponse.<List<CartResponse>>builder().data(cartService.getCartList()).build();
    }
}
