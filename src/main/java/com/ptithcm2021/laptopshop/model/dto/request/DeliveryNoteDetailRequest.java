package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNoteDetailRequest {
    @NotNull
    private Long productDetailId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Set<String> serialProductItemId;
}
