package com.ptithcm2021.laptopshop.model.dto.response.Order;

import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String code;
    private String userId;
    private OrderStatusEnum status;
    private LocalDateTime createdAt;
    private PaymentMethodEnum paymentMethod;
    private String note;
    private int totalQuantity;
    private int totalPrice;
    private Integer shopDiscount;
    private Integer userDiscount;
    private String address;
    private String phone;
    private String recipient;
    private List<OderDetailResponse> orderDetails;
    private String payUrl;
}
