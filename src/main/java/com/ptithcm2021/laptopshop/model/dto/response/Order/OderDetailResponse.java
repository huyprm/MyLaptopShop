package com.ptithcm2021.laptopshop.model.dto.response.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OderDetailResponse {
    private String thumbnail;
    private String title;
    private String ramValue;
    private String hardDriveValue;
    private int originalPrice;
    private int price;
    private int quantity;
    private String colorName;
    private String serialNumber;
    private Long productDetailId;
}
