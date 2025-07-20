package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PromotionResponse;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;

import java.util.List;

public interface PromotionService {
    PromotionResponse addPromotion(PromotionRequest promotionRequest);
    PromotionResponse updatePromotion(PromotionRequest promotionRequest, long id);
    void deletePromotion(long id);
    PromotionResponse getPromotion(long id);
    PageWrapper<PromotionResponse> getPromotions(PromotionTypeEnum promotionType, int page, int size);

    List<PromotionTypeEnum> getPromotionTypes();

    void collectPromotion(long id);

    List<PromotionResponse> myVouchers();
}
