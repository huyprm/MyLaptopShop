package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import org.springframework.security.access.prepost.PreAuthorize;

//@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PRODUCT')")
public interface ProductDetailService {

    ProductDetailResponse createProductDetail(ProductDetailRequest productDetailRequest);

    ProductDetailResponse createProductDetail(ProductDetailRequest productDetailRequest, Product product);

    ProductDetailResponse updateProductDetail(ProductDetailRequest productDetailRequest, long productDetailId);

    void deleteProductDetail(long productDetailId);

}
