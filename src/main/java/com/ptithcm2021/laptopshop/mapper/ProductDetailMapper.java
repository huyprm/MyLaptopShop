package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductDetailMapper {
    @Mapping(target = "productId", source = "product.id")
    ProductDetailResponse toResponse(ProductDetail productDetail);

    ProductDetail toProductDetail(ProductDetailRequest request);

    @Mapping(target = "productId", source = "product.id")
    ItemProductResponse toItemProductResponse(ProductDetail productDetail);

    void updateProductDetail(ProductDetailRequest productDetailRequest, @MappingTarget ProductDetail productDetail);
}
