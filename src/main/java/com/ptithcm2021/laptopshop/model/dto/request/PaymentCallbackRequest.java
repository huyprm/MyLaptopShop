package com.ptithcm2021.laptopshop.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCallbackRequest {
    private String orderId;   // Từ Momo gửi về
    private String transId;
    private String resultCode;
    private Integer amount;
}
