package com.ptithcm2021.laptopshop.model.dto.response.Product;

import java.util.List;

public class ItemProductDetailResponse {
    private int id;
    private String description;
    private Integer brandId;
    private Integer categoryId;
    private Integer seriesId;
    private List<ProductDetailResponse> productDetails;
}
