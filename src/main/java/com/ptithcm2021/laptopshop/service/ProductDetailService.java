package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ProductDetailService {
    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    ProductDetailResponse createProductDetail(ProductDetailRequest productDetailRequest);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    void createProductDetail(ProductDetailRequest productDetailRequest, Product product);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    ProductDetailResponse updateProductDetail(ProductDetailRequest productDetailRequest, long productDetailId);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    void deleteProductDetail(long productDetailId);

}
