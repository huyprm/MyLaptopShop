package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductDetailService productDetailService;

    @PostMapping("/create")
    public ApiResponse<ProductDetailResponse> createProductDetail(@RequestBody ProductDetailRequest request){
        return ApiResponse.<ProductDetailResponse>builder()
                .data(productDetailService.createProductDetail(request)).build();
    }

    @PatchMapping("/update/{id}")
    public ApiResponse<ProductDetailResponse> updateProductDetail(@PathVariable long id, @RequestBody ProductDetailRequest request){
        return ApiResponse.<ProductDetailResponse>builder()
                .data(productDetailService.updateProductDetail(request, id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductDetail(@PathVariable long id){
        productDetailService.deleteProductDetail(id);
        return ApiResponse.<Void>builder().message("Deleted product detail successful").build();
    }
}
