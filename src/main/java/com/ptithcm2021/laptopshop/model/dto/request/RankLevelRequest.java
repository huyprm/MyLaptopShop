package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankLevelRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private int minSpending;

    private int minOrder;

    @NotNull
    private int priority;

    private boolean active;

    private Long promotionId;
}
