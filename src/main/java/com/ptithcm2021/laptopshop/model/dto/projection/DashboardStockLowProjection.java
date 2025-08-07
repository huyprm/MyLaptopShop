package com.ptithcm2021.laptopshop.model.dto.projection;

import jakarta.persistence.criteria.CriteriaBuilder;

public interface DashboardStockLowProjection {
    String getProductDetailId();
    String getTitle();
    String getThumbnail();
    Integer getQuantity();
    Integer getOriginalPrice();
    Integer getDiscountPrice();

}
