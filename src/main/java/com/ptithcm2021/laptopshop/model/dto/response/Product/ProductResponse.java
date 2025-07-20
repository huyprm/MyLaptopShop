package com.ptithcm2021.laptopshop.model.dto.response.Product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ptithcm2021.laptopshop.model.dto.response.BrandResponse;
import com.ptithcm2021.laptopshop.model.dto.response.CategoryResponse;
import com.ptithcm2021.laptopshop.model.dto.response.SeriesResponse;
import com.ptithcm2021.laptopshop.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private int id;
    private String description;
    @JsonIgnoreProperties("series")
    private BrandResponse brand;
    private CategoryResponse category;
    private SeriesResponse series;
    private List<ProductDetailResponse> productDetails;
}
