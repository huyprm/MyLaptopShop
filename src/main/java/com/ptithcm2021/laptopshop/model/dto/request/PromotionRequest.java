package com.ptithcm2021.laptopshop.model.dto.request;

import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String code;
    @NotBlank
    private String description;
    @NotNull
    private Integer discountValue;
    @NotNull
    private DiscountUnitEnum discountUnit;
    @NotNull
    private PromotionTypeEnum promotionType;
    private Integer minOrderValue;
    private Integer maxDiscountValue;
    private Integer usageLimit;

    @NotNull
    private LocalDateTime startDate;

    private LocalDateTime endDate;
    private List<Long> productDetailIds;
    private List<String> userIds;
    private List<Integer> rankLevelIds;
}
