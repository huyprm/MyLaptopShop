package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String userId;
    @NotBlank
    private String address;
    @NotNull
    private Map<Long, Integer> productDetails;
    // Map of productDetailId to quantity
}
