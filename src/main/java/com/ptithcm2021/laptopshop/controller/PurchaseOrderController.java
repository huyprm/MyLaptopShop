package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.PurChaseOrderRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrderDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrderResponse;
import com.ptithcm2021.laptopshop.model.entity.PurchaseOrder;
import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import com.ptithcm2021.laptopshop.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/create")
    public ApiResponse<PurchaseOrderResponse> createPurchaseOrder(@RequestBody PurChaseOrderRequest purchaseOrder) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .data(purchaseOrderService.createPurchaseOrder(purchaseOrder)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PurchaseOrderResponse> getPurchaseOrderById(@PathVariable Long id) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .data(purchaseOrderService.getPurchaseOrderById(id)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<PurchaseOrderResponse> updatePurchaseOrder(@RequestBody @Valid PurChaseOrderRequest purchaseOrder,
                                                                  @PathVariable Long id) {
        return ApiResponse.<PurchaseOrderResponse>builder()
                .data(purchaseOrderService.updatePurchaseOrder(id, purchaseOrder)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping()
    public ApiResponse<PageWrapper<PurchaseOrderResponse>> getPurchaseOrders(@RequestParam(required = false) PurchaseOrderStatusEnum statusEnum,
                                                                             @RequestParam(defaultValue = "0") Integer page,
                                                                             @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.<PageWrapper<PurchaseOrderResponse>>builder()
                .data(purchaseOrderService.getPurchaseOrders(page, size, statusEnum)).build();
    }
}
