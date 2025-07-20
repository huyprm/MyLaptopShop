package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
    @NotBlank()
    private String name;
    private String description;
    private String image;
    private List<SeriesRequest> seriesRequests;
}
