package com.ptithcm2021.laptopshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponse {
    private int id;
    private String name;
    private String description;
    private String image;
    private List<SeriesResponse> series;
}
