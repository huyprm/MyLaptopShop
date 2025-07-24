package com.ptithcm2021.laptopshop.strategy.impl;

import com.ptithcm2021.laptopshop.model.dto.request.PaymentRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import com.ptithcm2021.laptopshop.strategy.PaymentStrategy;
import com.ptithcm2021.laptopshop.validator.PaymentHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@PaymentHandler(PaymentMethodEnum.MOMO)
@Component
@Getter
public class MomoPaymentStrategy implements PaymentStrategy {

    @Value("${momo.partner-code}")
    private String partnerCode;
    @Value("${momo.access-key}")
    private String accessKey;
    @Value("${momo.secret-key}")
    private String secretKey;
    @Value("${momo.endpoint}")
    private String endpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PaymentResponse pay(PaymentRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        String requestId = UUID.randomUUID().toString();
        String redirectUrl = "http://localhost:5173";
        String ipnUrl = "https://dev.api.mylaptopshop.me/api/payment/callback/momo";

        Map<String, String> rawDataMap = new LinkedHashMap<>();
        rawDataMap.put("amount", String.valueOf(request.getAmount()));
        rawDataMap.put("extraData", request.getUserId());
        rawDataMap.put("ipnUrl", ipnUrl);
        rawDataMap.put("orderId", String.valueOf(request.getOrderId()));
        rawDataMap.put("orderInfo", "Thanh toán đơn hàng #" + request.getOrderId());
        rawDataMap.put("partnerCode", partnerCode);
        rawDataMap.put("redirectUrl", redirectUrl);
        rawDataMap.put("requestId", requestId);
        rawDataMap.put("requestType", "captureWallet");

        String rawData = buildRawSignature(rawDataMap);
        String signature = hmacSHA256(rawData, secretKey);

        Map<String, Object> momoRequest = new HashMap<>(rawDataMap);
        momoRequest.put("signature", signature);
        momoRequest.put("language", "vi");

        return restTemplate.postForObject(endpoint, momoRequest, PaymentResponse.class);
    }

    @Override
    public void handleCallback() {
        // Xử lý callback từ Momo
        // 1. Kiểm tra orderId có tồn tại trong hệ thống không
        // 2. Kiểm tra transactionId có trùng không
        // 3. Kiểm tra kết quả thanh toán (resultCode)
        // 4. Cập nhật trạng thái đơn hàng
        // 5. Trả về ApiResponse thành công

    }

    private String buildRawSignature(Map<String, String> rawDataMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("accessKey=").append(accessKey).append("&");
        rawDataMap.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        sb.setLength(sb.length() - 1); // remove last '&'
        return sb.toString();
    }

    private String hmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes());

        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
