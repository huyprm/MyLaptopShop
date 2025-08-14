package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.projection.VoucherProjection;
import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionResponse;
import com.ptithcm2021.laptopshop.model.enums.PromotionStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import com.ptithcm2021.laptopshop.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionService promotionService;

    @Operation(
            summary = "Tạo chương trình khuyến mãi",
            description = """
        Tạo một promotion với loại tương ứng. Có 6 loại promotion:
        
        - USER_PROMOTION: cần truyền danh sách `userIds` trong trường `ids`
        - PRODUCT_DISCOUNT: cần truyền danh sách `productIds` trong trường `ids`
        - GIFT: có thể truyền hoặc không
        - SHOP_DISCOUNT, ALL_USER, ALL_PRODUCT: không yêu cầu `ids`
        """)
    @PostMapping("/create")
    public ApiResponse<PromotionResponse> create(@RequestBody @Valid PromotionRequest promotionRequest) {
        return ApiResponse.<PromotionResponse>builder()
                .data(promotionService.addPromotion(promotionRequest)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PromotionResponse> getPromotion(@PathVariable long id) {
        return ApiResponse.<PromotionResponse>builder().data(promotionService.getPromotion(id)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<PromotionResponse> updatePromotion(@RequestBody @Valid PromotionRequest promotionRequest, @PathVariable long id) {
        return ApiResponse.<PromotionResponse>builder()
                .data(promotionService.updatePromotion(promotionRequest, id)).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deletePromotion(@PathVariable long id) {
        promotionService.deletePromotion(id);
        return ApiResponse.<String>builder()
                .message("Promotion deleted successfully").build();
    }

    @GetMapping("/list")
    public ApiResponse<PageWrapper<PromotionResponse>> getPromotions(
            @RequestParam(required = false) PromotionTypeEnum promotionType,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) PromotionStatusEnum status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageWrapper<PromotionResponse>>builder()
                .data(promotionService.getPromotions(code, status, promotionType, page, size)).build();
    }

    @GetMapping("/types")
    public ApiResponse<List<PromotionTypeEnum>> getPromotionTypes() {
        return ApiResponse.<List<PromotionTypeEnum>>builder()
                .data(promotionService.getPromotionTypes()).build();
    }

    @PostMapping("/collect/{id}")
    public ApiResponse<String> collectPromotion(@PathVariable long id) {
        promotionService.collectPromotion(id);
        return ApiResponse.<String>builder()
                .message("Promotion collected successfully").build();
    }

    @GetMapping("/my-vouchers")
    public ApiResponse<List<VoucherProjection>> myVouchers() {
        return ApiResponse.<List<VoucherProjection>>builder()
                .data(promotionService.myVouchers()).build();
    }

    @GetMapping("/product-detail/{id}")
    public ApiResponse<List<PromotionResponse>> productPromotion(@PathVariable long id,
                                                                 @RequestParam PromotionStatusEnum promotionStatus) {
        return ApiResponse.<List<PromotionResponse>>builder()
                .message("Promotion redeemed successfully")
                .data(promotionService.getProductPromotions(id, promotionStatus))
                .build();
    }

    @GetMapping("details/{id}")
    public ApiResponse<PageWrapper<PromotionDetailResponse>> getPromotionDetailsByType(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageWrapper<PromotionDetailResponse>>builder()
                .data(promotionService.getPromotionDetailsByType(id, page, size)).build();
    }

    @GetMapping("/shop-promotion/is-active")
    public ApiResponse<PromotionResponse> getShopPromotionIsActive() {
        return ApiResponse.<PromotionResponse>builder()
                .data(promotionService.getShopPromotionsIsActive()).build();
    }
}
