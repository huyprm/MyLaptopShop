package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.response.CartResponse;
import com.ptithcm2021.laptopshop.model.entity.Cart;
import com.ptithcm2021.laptopshop.model.entity.CartId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = ColorMapper.class)
public interface CartMapper {
    @Mapping(target = "productId", source = "productDetail.product.id")
    @Mapping(target = "productDetailId", source = "productDetail.id")
    @Mapping(target = "thumbnail", source = "productDetail.thumbnail")
    @Mapping(target = "title", source = "productDetail.title")
    @Mapping(target = "originalPrice", source = "productDetail.originalPrice")
    @Mapping(target = "discountPrice", source = "productDetail.discountPrice")
    @Mapping(target = "color", source = "productDetail.color")
    @Mapping(target = "itemImage", source = "productDetail.images", qualifiedByName = "itemImage")
    @Mapping(target = "ramValue", source = "productDetail.config.ramValue")
    @Mapping(target = "hardDriveValue", source = "productDetail.config.hardDriveValue")
    CartResponse toCartResponse(Cart cart);

    @Named("itemImage")
    default String firstImage(List<String> images){
        return  (images != null && !images.isEmpty()) ? images.getFirst() : null;
    }
}
