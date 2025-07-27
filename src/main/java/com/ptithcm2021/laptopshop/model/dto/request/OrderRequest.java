package com.ptithcm2021.laptopshop.model.dto.request;

import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String userId;
    @NotNull
    private Long addressId;

    private String note;
    @NotEmpty
    private List<OrderDetailRequest> detailRequest;
    private Long userPromotionId;
    private Long shopPromotionId;
    private PaymentMethodEnum paymentMethod;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class OrderDetailRequest {
        @NotNull
        private Long productDetailId;
        @NotNull
        private Integer quantity;
        private Long productPromotionId;

    }
}
