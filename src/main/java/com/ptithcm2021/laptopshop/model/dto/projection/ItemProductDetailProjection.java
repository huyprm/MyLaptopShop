package com.ptithcm2021.laptopshop.model.dto.projection;

import com.ptithcm2021.laptopshop.model.dto.response.ColorResponse;

public interface ItemProductDetailProjection {
    long getId();
    int getOriginalPrice();
    int getDiscountPrice();
    String getTitle();
    String getThumbnail();
    String getName();
    String getHex();
}
