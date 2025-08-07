package com.ptithcm2021.laptopshop.model.dto.projection;

import java.time.LocalDate;

public interface DashboardRevenueProjection {
    LocalDate getRevenueDate();
    Long getRevenue();
}
