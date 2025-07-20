package com.ptithcm2021.laptopshop.model.dto.request.Product;

import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigRequest {
    @NotBlank
    private String cpu;
    @NotBlank
    private String madeIn;
    @NotBlank
    private String displaySize;
    @NotBlank
    private String graphicCard;
    @NotBlank
    private String ram;
    @NotBlank
    private String ramValue;
    @NotBlank
    private String weight;
    @NotBlank
    private String hardDrive;
    @NotBlank
    private String hardDriveValue;

    private String nameConfig;

    private Long productDetailId;
}
