package com.ptithcm2021.laptopshop.model.dto.response.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemProductResponse {
    private long productId;
    private long productDetailId;
    private int originalPrice;
    private int discountPrice;
    private String thumbnail;
    private double totalRating;
    private int soldQuantity;
    private String title;
    private String warrantyProd;
    private String itemImage;
    private int quantity;
    private LocalDateTime createdDate;
    private String promotionIdMaxDiscount;
    private boolean active;
}
