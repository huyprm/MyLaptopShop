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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {
    private Long id;
    private String code;
    private String userId;
    private OrderStatusEnum status;
    private LocalDateTime createdDate;
    private PaymentMethodEnum paymentMethod;
    private String note;
    private int totalQuantity;
    private int totalPrice;
    private String fullName;
}
