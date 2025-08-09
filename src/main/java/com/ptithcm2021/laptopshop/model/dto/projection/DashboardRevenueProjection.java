package com.ptithcm2021.laptopshop.model.dto.projection;

import java.time.LocalDate;
import java.time.YearMonth;

public interface DashboardRevenueProjection {
    String getYearMonth();
    Long getMonthlyRevenue();
    Long getMonthlyGrossProfit();
    Long getMonthlyTotalCost();
}
