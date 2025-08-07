package com.ptithcm2021.laptopshop.model.dto.projection;

public interface DashboardCustomerTopProjection {
    String getId();
    String getFullName();
    String getEmail();
    Long getTotalSpent();
    Long getTotalOrders();
}
