package com.ptithcm2021.laptopshop.model.dto.response.Promotion;

import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private PromotionTypeEnum promotionType;
    private Integer discountValue;
    private DiscountUnitEnum discountUnit;
    private Integer minOrderValue;
    private Integer maxDiscountValue;
    private Integer usageLimit;
    private Integer usageCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String userId;
    private String username;
    private LocalDateTime createdAt;
//    private List<Long> productDetailIds;
}
