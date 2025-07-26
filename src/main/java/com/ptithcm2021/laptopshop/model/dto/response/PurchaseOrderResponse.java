package com.ptithcm2021.laptopshop.model.dto.response;

import com.ptithcm2021.laptopshop.model.dto.request.PurChaseOrderRequest;
import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
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
public class PurchaseOrderResponse {
    private Long id;
    private String supplierName;
    private String userOrderName;
    private String userId;
    private String code;
    private LocalDateTime orderDate;
    private PurchaseOrderStatusEnum status;
    private String note;
    private int totalQuantity;
    private List<PurchaseOrderDetailResponse> details;
}
