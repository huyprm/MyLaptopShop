package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptNoteRequest {
    private String note;
    @NotBlank
    private String purchaseOrderCode;
    @NotNull
    private LocalDate receivedDate;
    @NotEmpty
    private List<GoodsReceiptNoteDetailRequest> detailRequestList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsReceiptNoteDetailRequest {
        @NotNull
        private Long productDetailId;
        @NotNull
        private Long purchaseOrderDetailId;
        @Min(1)
        private Integer quantity;
        @NotBlank
        private List<String> serialNumber;
        @Min(0)
        private Integer unitPrice;
    }
}
