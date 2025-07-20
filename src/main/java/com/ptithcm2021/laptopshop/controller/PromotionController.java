package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PromotionResponse;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import com.ptithcm2021.laptopshop.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-promotions")
public class PromotionController {
    private final PromotionService promotionService;

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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageWrapper<PromotionResponse>>builder()
                .data(promotionService.getPromotions(promotionType, page, size)).build();
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
    public ApiResponse<List<PromotionResponse>> myVouchers() {
        return ApiResponse.<List<PromotionResponse>>builder()
                .data(promotionService.myVouchers()).build();
    }

}
