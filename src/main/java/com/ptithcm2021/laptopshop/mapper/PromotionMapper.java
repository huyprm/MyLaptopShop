package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PromotionResponse;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.model.entity.ProductPromotion;
import com.ptithcm2021.laptopshop.model.entity.Promotion;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ProductDetailMapper.class})
public interface PromotionMapper {
    Promotion toPromotion(PromotionRequest promotionRequest);

    @Mapping(
            target = "productDetailIds",
            source = "productPromotions"
    )
    @Mapping(target = "userId", source = "userCreate.id")
    @Mapping(target = "username", source = "userCreate", qualifiedByName = "fullName")
    PromotionResponse toPromotionResponse(Promotion promotion);

    void updatePromotion(PromotionRequest promotionRequest, @MappingTarget Promotion promotion);
    default Long map(ProductPromotion promotion) {
        return promotion.getProductDetail().getId();
    }

    @Named("fullName")
    default String getFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

}
