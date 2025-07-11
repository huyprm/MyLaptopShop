package com.ptithcm2021.laptopshop.model.dto.request.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    private String description;
    private Integer brandId;
    private Integer categoryId;
    private Integer seriesId;
    private Map<Long, ProductDetailRequest> productDetailRequestMap;
}
