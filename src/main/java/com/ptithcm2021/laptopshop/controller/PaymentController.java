package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.event.EventPublisherHelper;
import com.ptithcm2021.laptopshop.event.PaymentSuccessEvent;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import com.ptithcm2021.laptopshop.service.OrderService;
import com.ptithcm2021.laptopshop.util.ValidPaymentSignature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    @Value("${momo.secret-key}")
    private String secretKey;
    @Value("${momo.access-key}")
    private String accessKey;

    private final EventPublisherHelper publisher;
    private final OrderService orderService;

    @PostMapping("/callback/momo")
    public ApiResponse<Void> momo(@RequestBody Map<String, String> request) throws NoSuchAlgorithmException, InvalidKeyException {

        // Xử lý callback từ Momo
        if (!ValidPaymentSignature.isValidMomoSignature(request, secretKey, accessKey)){
            throw new RuntimeException("Invalid Momo signature");
        }
        int resultCode = Integer.parseInt(request.get("resultCode"));
        Long orderId = Long.parseLong(request.get("orderId"));
        String userId = request.get("extraData");
        Integer amount = Integer.parseInt(request.get("amount"));
        if(resultCode == 0){
            publisher.publish(new PaymentSuccessEvent(orderId, userId, amount));

        } else {
           return ApiResponse.<Void>builder()
                    .message("Payment failed")
                    .build();
        }

        return ApiResponse.<Void>builder()
                .message("OK")
                .build();
    }

    @GetMapping("/{oderId}")
    public ApiResponse<PaymentResponse> getPaymentStatus(@PathVariable Long oderId) throws NoSuchAlgorithmException, InvalidKeyException {
        return  ApiResponse.<PaymentResponse>builder()
                .data(orderService.getPaymentInfo(oderId))
                .build();
    }

}
