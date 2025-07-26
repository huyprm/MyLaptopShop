package com.ptithcm2021.laptopshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptNoteDetailResponse {
    private String productTitle;
    private Integer quantity;
    private String serialNumber;
    private Integer unitPrice;
}
