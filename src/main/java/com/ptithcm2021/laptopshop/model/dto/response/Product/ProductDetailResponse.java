package com.ptithcm2021.laptopshop.model.dto.response.Product;

import com.ptithcm2021.laptopshop.model.dto.response.ColorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private long id;
    private long productId;
    private ColorResponse color;
    private int originalPrice;
    private int discountPrice;
    private String slug;
    private String title;
    private String warrantyProd;
    private String thumbnail;
    private double totalRating;
    private int soldQuantity;
    private List<String> images;
    private ConfigResponse config;
    private Integer quantity;
    private String promotionIdMaxDiscount;
    private LocalDateTime createdDate;
    private boolean active;
}
