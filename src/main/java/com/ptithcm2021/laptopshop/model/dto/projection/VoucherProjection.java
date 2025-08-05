package com.ptithcm2021.laptopshop.model.dto.projection;

import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public interface VoucherProjection {
    Long getId();
    String getName();
    String getCode();
    String getDescription();
    PromotionTypeEnum getPromotionType();
    Integer getDiscountValue();
    DiscountUnitEnum getDiscountUnit();
    Integer getMaxDiscountValue();
    Integer getMinOrderValue();
    Integer getUsageLimit();
    Integer getUsageCount();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    Boolean getUsed();
}

