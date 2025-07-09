package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ColorRequest {
    @NotBlank()
    private String name;

    @NotBlank
    private String hex;
}
