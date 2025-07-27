package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.DeliveryNoteDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.request.DeliveryNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.entity.DeliveryNote;
import com.ptithcm2021.laptopshop.service.DeliveryNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery-notes")
@RequiredArgsConstructor
public class DeliveryNoteController {
    private final DeliveryNoteService deliveryNoteService;

    @PostMapping("/create")
    public ApiResponse<DeliveryNoteResponse> createDeliveryNote(@RequestBody DeliveryNoteRequest request){
        return ApiResponse.<DeliveryNoteResponse>builder()
                .data(deliveryNoteService.addDeliveryNote(request))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<DeliveryNoteResponse> updateDeliveryNote(@PathVariable long id, @RequestBody DeliveryNoteRequest request) {
        return ApiResponse.<DeliveryNoteResponse>builder()
                .data(deliveryNoteService.updateDeliveryNote(id, request))
                .build();
    }

    @PutMapping("/confirm/{id}")
    public ApiResponse<Void> confirmDeliveryNote(@PathVariable long id) {
        deliveryNoteService.confirmDeliveryNote(id);
        return ApiResponse.<Void>builder()
                .message("Delivery note confirmed successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<PageWrapper<DeliveryNoteResponse>> getDeliveryNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageWrapper<DeliveryNoteResponse>>builder()
                .data(deliveryNoteService.getDeliveryNotes(page, size))
                .build();
    }

    @GetMapping("/product-detail/{orderCode}")
    public ApiResponse<PageWrapper<DeliveryNoteResponse>> getDeliveryNotesByProductDetailId(
            @PathVariable String orderCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageWrapper<DeliveryNoteResponse>>builder()
                .data(deliveryNoteService.getDeliveryNotesByProductDetailId(page, size, orderCode))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DeliveryNoteResponse> getDeliveryNoteById(@PathVariable long id) {
        return ApiResponse.<DeliveryNoteResponse>builder()
                .data(deliveryNoteService.getDeliveryNoteById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDeliveryNote(@PathVariable long id) {
        deliveryNoteService.deleteDeliveryNote(id);
        return ApiResponse.<Void>builder()
                .message("Delivery note deleted successfully")
                .build();
    }
}
