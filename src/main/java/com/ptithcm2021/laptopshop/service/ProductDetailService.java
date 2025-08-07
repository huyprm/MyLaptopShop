package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.projection.DashboardStockLowProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductDetailProjection;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.UpdateProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PRODUCT')")
public interface ProductDetailService {

    ProductDetailResponse createProductDetail(ProductDetailRequest productDetailRequest);

    ProductDetailResponse createProductDetail(ProductDetailRequest productDetailRequest, Product product);

    void updateProductDetail(UpdateProductDetailRequest request, Product product);

    void deleteProductDetail(long productDetailId);

    void handleAddRating(Long productDetailId, int rating);
    void handleUpdateRating(Long productDetailId, int oldRating, int newRating);
    void handleDeleteRating(Long productDetailId, int rating);

    PageWrapper<ItemProductDetailProjection> getProductDetails(String keyword, int page, int size);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER')")
    List<DashboardStockLowProjection> getStockLowProducts(int limit, int threshold);
}
