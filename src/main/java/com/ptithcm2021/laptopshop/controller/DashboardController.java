package com.ptithcm2021.laptopshop.controller;

import com.cloudinary.Api;
import com.ptithcm2021.laptopshop.model.dto.projection.*;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.service.OrderService;
import com.ptithcm2021.laptopshop.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final OrderService orderService;
    private final ProductDetailService productDetailService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryProjection> getDashboardSummary() {
        return ApiResponse.<DashboardSummaryProjection>builder()
                .message("Dashboard Summary")
                .data(orderService.getDashboardSummary())
                .build();
    }

    @GetMapping("/revenue")
    public ApiResponse<DashboardRevenueProjection> getDashboard(
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {

        LocalDate now = LocalDate.now();

        if (to == null) {
            to = now;
        }

        if (from == null) {
            from = to.minusMonths(5); // lấy 6 tháng gần nhất
        }

        return ApiResponse.<DashboardRevenueProjection>builder()
                .message("Dashboard Revenue")
                .data(orderService.getDashboardRevenue(from, to))
                .build();
    }

    @GetMapping("/top-products")
    public ApiResponse<List<DashboardTopProductProjection>> getTopProducts(@RequestParam(defaultValue = "10") int limit) {
        // This method should return the top products based on sales or views.
        return ApiResponse.<List<DashboardTopProductProjection>>builder()
                .message("Top Products")
                .data(orderService.getDashboardTopProducts(limit))
                .build();
    }

    @GetMapping("/customers/top")
    public ApiResponse<List<DashboardCustomerTopProjection>> getTopCustomers(@RequestParam (defaultValue = "10") int limit) {
        // This method should return the top customers based on purchase history.
        return ApiResponse.<List<DashboardCustomerTopProjection>>builder()
                .message("Top Customers")
                .data(orderService.getDashboardCustomerTop(limit))
                .build();
    }

    @GetMapping("/stock/low")
    public  ApiResponse<List<DashboardStockLowProjection>> getStockLow(@RequestParam(defaultValue = "10") int limit,
                                                                       @RequestParam(defaultValue = "1") int threshold) {
        // This method should return products that are low in stock.
        return ApiResponse.<List<DashboardStockLowProjection>>builder()
                .message("Low Stock Products")
                .data(productDetailService.getStockLowProducts(limit, threshold))
                .build();
    }


}
