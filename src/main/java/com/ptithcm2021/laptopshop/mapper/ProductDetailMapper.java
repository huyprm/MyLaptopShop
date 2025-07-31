package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.entity.Inventory;
import com.ptithcm2021.laptopshop.model.entity.Product;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductDetailMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "quantity", source = "inventory.quantity")
    ProductDetailResponse toResponse(ProductDetail productDetail);

    @Mapping(target = "discountPrice", source = "originalPrice")
    ProductDetail toProductDetail(ProductDetailRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "itemImage", source = "images", qualifiedByName = "itemImage")
    ItemProductResponse toItemProductResponse(ProductDetail productDetail);

    void updateProductDetail(ProductDetailRequest productDetailRequest, @MappingTarget ProductDetail productDetail);

    @Named("itemImage")
    default String firstImage(List<String> images){
        return  (images != null && !images.isEmpty()) ? images.getFirst() : null;
    }


}
