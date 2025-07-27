package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.OrderRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.OrderResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import com.ptithcm2021.laptopshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ApiResponse.<OrderResponse>builder()
                .message("Order created successfully")
                .data(orderResponse)
                .build();
    }


    @Operation(summary = "Change order status", description = "Users can use it to cancel orders, sellers can approve and confirm orders.")
    @PutMapping("/change-status/{id}")
    public ApiResponse<Void> cancelOrder(@RequestParam OrderStatusEnum status,
                                         @PathVariable Long id) {
        orderService.changeOrderStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Order status updated successfully")
                .build();
    }

//    @PutMapping("/change-status/shipping/{id}")
//    public ApiResponse<Void> changeOrderStatusToShipping(@PathVariable Long id,
//                                                          @RequestBody Map<Long, List<String>> serialNumbers) {
//        orderService.changeOrderStatusToShipping(id, serialNumbers);
//        return ApiResponse.<Void>builder()
//                .message("Order status changed to shipping successfully")
//                .build();
//    }

    @PutMapping("/{oderId}")
    public ApiResponse<PaymentResponse> changPaymentMethod(@PathVariable Long oderId,
                                                           @RequestParam PaymentMethodEnum method) throws NoSuchAlgorithmException, InvalidKeyException {
        return  ApiResponse.<PaymentResponse>builder()
                .data(orderService.changePaymentMethod(oderId, method))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ApiResponse.<OrderResponse>builder()
                .message("Order retrieved successfully")
                .data(orderResponse)
                .build();
    }

    @GetMapping("/status")
    public ApiResponse<OrderStatusEnum[]> getAllOrderStatus() {
        OrderStatusEnum[] statuses = orderService.getAllOrderStatus().toArray(new OrderStatusEnum[0]);
        return ApiResponse.<OrderStatusEnum[]>builder()
                .message("Order statuses retrieved successfully")
                .data(statuses)
                .build();
    }

    @GetMapping("/payment-methods")
    public ApiResponse<String[]> getPaymentMethods() {
        String[] paymentMethods = orderService.getPaymentMethod().stream()
                .map(Enum::name)
                .toArray(String[]::new);
        return ApiResponse.<String[]>builder()
                .message("Payment methods retrieved successfully")
                .data(paymentMethods)
                .build();
    }

    @GetMapping("/user")
    public ApiResponse<PageWrapper<OrderResponse>> getAllOrdersByUserId(@RequestParam(required = false) OrderStatusEnum status,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        var pageWrapper = orderService.getAllOrdersByUserId(status, page, size);
        return ApiResponse.<PageWrapper<OrderResponse>>builder()
                .message("Orders retrieved successfully")
                .data(pageWrapper)
                .build();
    }
}
