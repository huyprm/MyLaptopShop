package com.ptithcm2021.laptopshop.model.dto.response;

import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankLevelResponse {
    private int id;
    private String name;
    private String description;
    private int minSpending;
    private int minOrder;
    private int priority;
    private PromotionResponse promotion;
}