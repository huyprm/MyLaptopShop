package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductDetailProjection;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
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

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductDetail(@PathVariable long id){
        productDetailService.deleteProductDetail(id);
        return ApiResponse.<Void>builder().message("Deleted product detail successful").build();
    }

    @GetMapping("/search")
    public ApiResponse<PageWrapper<ItemProductDetailProjection>> searchProductDetails(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String title) {
        PageWrapper<ItemProductDetailProjection> response = productDetailService.getProductDetails(title, page, size);
        return ApiResponse.<PageWrapper<ItemProductDetailProjection>>builder().data(response).build();
    }
}
