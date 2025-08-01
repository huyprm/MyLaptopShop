package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.GoodsReceiptNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.service.GoodsReceiptNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods-receipt-notes")
public class GoodsReceiptNoteController {
    private final GoodsReceiptNoteService goodsReceiptNoteService;

    @PostMapping("/create")
    public ApiResponse<GoodsReceiptNoteResponse> createGoodsReceiptNote(@RequestBody GoodsReceiptNoteRequest goodsReceiptNoteRequest) {
        return ApiResponse.<GoodsReceiptNoteResponse>builder()
                .data(goodsReceiptNoteService.createGRNResponse(goodsReceiptNoteRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteGoodsReceiptNote(@PathVariable Long id) {
        goodsReceiptNoteService.removeGRN(id);
        return ApiResponse.<Void>builder()
                .message("Goods Receipt Note deleted successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<GoodsReceiptNoteResponse> getGoodsReceiptNoteById(@PathVariable Long id) {
        return ApiResponse.<GoodsReceiptNoteResponse>builder()
                .data(goodsReceiptNoteService.getGRNById(id))
                .build();
    }

    @GetMapping("/search-by-code")
    public ApiResponse<PageWrapper<GoodsReceiptNoteListResponse>> getGoodsReceiptNoteListByPurchaseOrderCode(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String grnCode) {
        return ApiResponse.<PageWrapper<GoodsReceiptNoteListResponse>>builder()
                .data(goodsReceiptNoteService.getGRNListByPurchaseOrderCode(page, size, grnCode))
                .build();
    }

    @GetMapping("/list-by-purchase-order")
    public ApiResponse<PageWrapper<GoodsReceiptNoteResponse>> getGoodsReceiptNoteByPurchaseOrderId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam() long purchaseOrderId) {
        return ApiResponse.<PageWrapper<GoodsReceiptNoteResponse>>builder()
                .data(goodsReceiptNoteService.getGRNListByPurchaseOrderId(page, size, purchaseOrderId))
                .build();
    }
}
