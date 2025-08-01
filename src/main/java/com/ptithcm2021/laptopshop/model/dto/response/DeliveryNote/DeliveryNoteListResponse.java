package com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote;

import com.ptithcm2021.laptopshop.model.enums.DeliveryNoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryNoteListResponse {
    private long id;
    private String note;
    private String code;
    private String orderCode;
    private DeliveryNoteStatus status;
    private LocalDate date;
    private int totalQuantity;
    private String staffName;
    private String staffId;
}
