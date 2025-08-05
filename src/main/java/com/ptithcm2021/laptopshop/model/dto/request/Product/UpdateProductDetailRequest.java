package com.ptithcm2021.laptopshop.model.dto.request.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDetailRequest {
    @NotNull
    private Long id;

    @NotNull
    private Integer colorId;

    @NotNull
    private Integer originalPrice;

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
}
