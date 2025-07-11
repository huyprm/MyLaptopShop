package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.response.CartResponse;
import com.ptithcm2021.laptopshop.model.entity.Cart;
import com.ptithcm2021.laptopshop.model.entity.CartId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "productId", source = "productDetail.id")
    @Mapping(target = "thumbnail", source = "productDetail.thumbnail")
    @Mapping(target = "title", source = "productDetail.title")
    @Mapping(target = "originalPrice", source = "productDetail.originalPrice")
    @Mapping(target = "discountPrice", source = "productDetail.discountPrice")
    CartResponse toCartResponse(Cart cart);
}
