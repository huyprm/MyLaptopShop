package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    @NotNull
    private Long productDetailId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
