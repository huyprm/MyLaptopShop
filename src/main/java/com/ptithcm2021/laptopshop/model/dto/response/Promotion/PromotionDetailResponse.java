package com.ptithcm2021.laptopshop.model.dto.response.Promotion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionDetailResponse {
    private String notice;
    private Long productDetailId;
    private String title;
    private String thumbnail;
    private Integer originalPrice;
    private String userId;
    private String lastName;
    private String firstName;
    private String email;
}
