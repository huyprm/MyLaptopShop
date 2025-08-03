package com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder;

import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderListResponse {
    private Long id;
    private String supplierName;
    private int supplierId;
    private String userOrderName;
    private String userId;
    private String code;
    private LocalDateTime orderDate;
    private PurchaseOrderStatusEnum status;
    private String note;
    private int totalQuantity;
}
