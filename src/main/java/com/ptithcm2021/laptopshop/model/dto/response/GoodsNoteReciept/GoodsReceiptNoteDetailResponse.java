package com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptNoteDetailResponse {
    private String productTitle;
    private String thumbnail;
    private Integer quantity;
    private String serialNumber;
    private Integer unitPrice;
}
