package com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryNoteDetailResponse {
    private String productTitle;
    private Integer quantity;
    private String serialNumber;
    private String thumbnail;
}
