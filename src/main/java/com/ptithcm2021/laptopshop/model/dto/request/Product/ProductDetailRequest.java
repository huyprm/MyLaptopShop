package com.ptithcm2021.laptopshop.model.dto.request.Product;

import com.ptithcm2021.laptopshop.model.entity.Color;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailRequest {
    @NotNull
    private Integer colorId;

    @NotNull
    private Integer originalPrice;

    @NotNull
    private Integer discountPrice;

    @NotBlank
    private String slug;

    @NotBlank
    private String title;

    @NotBlank
    private String thumbnail;

    @NotBlank
    private String warrantyProd;

    @NotNull
    private List<String> images;

    private ConfigRequest configRequest;

    private Long productId;
}
