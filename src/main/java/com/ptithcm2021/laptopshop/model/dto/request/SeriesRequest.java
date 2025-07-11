package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeriesRequest {
    @NotBlank()
    private String name;
    private String description;
}
