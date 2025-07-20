package com.ptithcm2021.laptopshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankUserResponse {
    private RankLevelResponse currentRank;
    private RankLevelResponse nextRank;
    private int amountUsed;
    private int amountOrder;
    private int spendingToNextRank;
    private int ordersToNextRank;

}
