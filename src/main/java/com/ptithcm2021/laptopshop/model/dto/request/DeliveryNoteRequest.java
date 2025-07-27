package com.ptithcm2021.laptopshop.model.dto.request;

import com.ptithcm2021.laptopshop.model.enums.DeliveryNoteStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryNoteRequest {
    private String note;
    @NotNull
    private String orderCode;
    @NotNull
    private DeliveryNoteStatus status;
    @NotEmpty
    private List<DeliveryNoteDetailRequest> details;
}
