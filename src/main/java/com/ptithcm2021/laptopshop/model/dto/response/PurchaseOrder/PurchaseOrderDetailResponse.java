package com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderDetailResponse {
    private Long id;
    private Long productDetailId;
    private String title;
    private String thumbnail;
    private Integer quantity;
    private Integer unitCost;
}
