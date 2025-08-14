package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.projection.VoucherProjection;
import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionResponse;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.model.enums.PromotionStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import jakarta.annotation.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.function.Consumer;

public interface PromotionService {
    @PreAuthorize("hasAnyAuthority('PERM_PROMOTION', 'SCOPE_OWNER')")
    PromotionResponse addPromotion(PromotionRequest promotionRequest);

    @PreAuthorize("hasAnyAuthority('PERM_PROMOTION', 'SCOPE_OWNER')")
    PromotionResponse updatePromotion(PromotionRequest promotionRequest, long id);

    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_PROMOTION', 'SCOPE_OWNER')")
    void deletePromotion(long id);

    PromotionResponse getPromotion(long id);

    PageWrapper<PromotionResponse> getPromotions(String keyword, PromotionStatusEnum status, PromotionTypeEnum promotionType, int page, int size);

    List<PromotionTypeEnum> getPromotionTypes();

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    void collectPromotion(long id);

    List<VoucherProjection> myVouchers();

    List<PromotionResponse> getProductPromotions(long id, PromotionStatusEnum statusEnum);
    PromotionResponse getShopPromotionsIsActive();

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    int applyPromotion(Long promotionId, String userId, int totalAmount, Consumer<Integer> setDiscountFn, @Nullable ProductDetail productDetail);

    void handlePromotionUserExpiredOrUsage();

    PageWrapper<PromotionDetailResponse> getPromotionDetailsByType(long id, int page, int size);
}
