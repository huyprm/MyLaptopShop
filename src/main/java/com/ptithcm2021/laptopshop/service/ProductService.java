package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.ProductSearchProjection;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductFilterRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.UpdateProductRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductResponse;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ProductService {
    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    ProductResponse createProduct(ProductRequest request);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    ProductResponse updateProduct(UpdateProductRequest request, long productId);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    void deleteProduct(long id);

    ProductResponse getProduct(long id);

    PagedModel<ProductResponse> getProducts(Pageable pageable);

    PagedModel<ItemProductProjection> getItemProducts(Pageable pageable);
    PagedModel<ItemProductResponse> getItemProductsFilter(int size, int page, ProductFilterRequest request);
    PagedModel<ItemProductProjection> searchProduct(String keyword, Pageable pageable);

    List<String>getSortOptions();
}
