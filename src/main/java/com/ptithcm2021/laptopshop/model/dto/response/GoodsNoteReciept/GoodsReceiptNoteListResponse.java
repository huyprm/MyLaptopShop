package com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsReceiptNoteListResponse {
    private Long id;
    private String code;
    private LocalDate receivedDate;
    private String note;
    private String staffName;
    private String staffId;
    private String purchaseOrderCode;
    private Integer totalQuantity;
}
