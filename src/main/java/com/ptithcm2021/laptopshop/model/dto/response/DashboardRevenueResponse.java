package com.ptithcm2021.laptopshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardRevenueResponse {

    private long monthlyRevenue;
    private long monthlyGrossProfit;
    private long monthlyTotalCost;
    private String yearMonth;
}
