package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.DeliveryNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.enums.DeliveryNoteStatus;
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

    @GetMapping("/search")
    public ApiResponse<PageWrapper<DeliveryNoteListResponse>> getDeliveryNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) DeliveryNoteStatus status) {
        return ApiResponse.<PageWrapper<DeliveryNoteListResponse>>builder()
                .data(deliveryNoteService.getDeliveryNotesByCode(page, size, orderCode, status))
                .build();
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<PageWrapper<DeliveryNoteResponse>> getDeliveryNotesByOrderId(
            @PathVariable long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageWrapper<DeliveryNoteResponse>>builder()
                .data(deliveryNoteService.getDeliveryNotesByOrderId(page, size, orderId))
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
