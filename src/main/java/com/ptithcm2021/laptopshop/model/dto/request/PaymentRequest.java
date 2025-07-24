package com.ptithcm2021.laptopshop.model.dto.request;

import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private PaymentMethodEnum paymentMethod;
    @NotNull
    private Integer amount;
    @NotBlank
    private String userId;
}
