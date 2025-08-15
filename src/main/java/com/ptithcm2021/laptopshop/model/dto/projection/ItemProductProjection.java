package com.ptithcm2021.laptopshop.model.dto.projection;

public interface ItemProductProjection {
    Long getProductId();

    Integer getOriginalPrice();

    Integer getDiscountPrice();

    String getThumbnail();

    Double getTotalRating();

    Integer getSoldQuantity();

    String getTitle();

    String getWarrantyProd();

    String getItemImage();

}
