package com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote;

import com.ptithcm2021.laptopshop.model.enums.DeliveryNoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryNoteResponse {
    private long id;
    private String note;
    private String code;
    private String orderCode;
    private DeliveryNoteStatus status;
    private LocalDate date;
    private int totalQuantity;
    private String staffName;
    private String staffId;
    private List<DeliveryNoteDetailResponse> deliveryNoteDetails;
}
