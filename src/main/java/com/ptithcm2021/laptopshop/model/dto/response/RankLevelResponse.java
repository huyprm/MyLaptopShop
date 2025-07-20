package com.ptithcm2021.laptopshop.model.dto.response;

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