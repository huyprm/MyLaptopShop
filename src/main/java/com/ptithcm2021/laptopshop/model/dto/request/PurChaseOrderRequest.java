package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurChaseOrderRequest {
    @NotNull
    private Integer supplierId;
    private String note;

    @NotEmpty
    private List<PurchaseOrderDetailRequest> details;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PurchaseOrderDetailRequest {
        @NotNull
        private Long productDetailId;
        @Min(1)
        private Integer quantity;
        @Min(0)
        private Integer unitCost;
    }
}
