package com.ptithcm2021.laptopshop.model.dto.projection;

public interface ProductSearchProjection {
    Long getProductId();
    String getBrandName();
    String getSeriesName();
    String getCategoryName();
    String getTitle();
}
