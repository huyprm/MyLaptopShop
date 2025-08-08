package com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsReceiptNoteDetailResponse {
    private String productTitle;
    private String thumbnail;
    private Integer quantity;
    private List<String> serialNumbers;
    private Integer unitPrice;
}
