package com.ptithcm2021.laptopshop.model.dto.projection;

public interface DashboardTopProductProjection {
    String getProductDetailId();
    String getProductTitle();
    String getProductThumbnail();
    Long getTotalSold();
    Long getRevenue();
}
