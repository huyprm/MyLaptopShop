package com.ptithcm2021.laptopshop.model.dto.response;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private long productId;
    private long productDetailId;
    private Long productPromotionId;
    private String thumbnail;
    private String title;
    private int originalPrice;
    private int discountPrice;
    private int quantity;
    private ColorResponse color;
    private String itemImage;
    private String ramValue;
    private String hardDriveValue;
}
