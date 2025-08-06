package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionResponse;
import com.ptithcm2021.laptopshop.model.entity.ProductPromotion;
import com.ptithcm2021.laptopshop.model.entity.Promotion;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.entity.UserPromotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ProductDetailMapper.class})
public interface PromotionMapper {
    Promotion toPromotion(PromotionRequest promotionRequest);

//    @Mapping(
//            target = "productDetailIds",
//            source = "productPromotions"
//    )
    @Mapping(target = "userId", source = "userCreate.id")
    @Mapping(target = "username", source = "userCreate", qualifiedByName = "fullName")
    PromotionResponse toPromotionResponse(Promotion promotion);

    void updatePromotion(PromotionRequest promotionRequest, @MappingTarget Promotion promotion);

//    default Long map(ProductPromotion promotion) {
//        return promotion.getProductDetail().getId();
//    }

    @Named("fullName")
    default String getFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "email", source = "user.email")
    PromotionDetailResponse toPromotionDetailResponse(UserPromotion userPromotion);

    @Mapping(target = "productDetailId", source = "productDetail.id")
    @Mapping(target = "title", source = "productDetail.title")
    @Mapping(target = "thumbnail", source = "productDetail.thumbnail")
    @Mapping(target = "originalPrice", source = "productDetail.originalPrice")
    PromotionDetailResponse toPromotionDetailResponse(ProductPromotion productPromotion);

    PromotionDetailResponse toPromotionDetailResponse(String notice);
}
