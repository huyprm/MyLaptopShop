package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.projection.*;
import com.ptithcm2021.laptopshop.model.dto.request.OrderRequest;
import com.ptithcm2021.laptopshop.model.dto.response.DashboardRevenueResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Order.OrderListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Order.OrderResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface OrderService {
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    OrderResponse createOrder(OrderRequest orderRequest) throws NoSuchAlgorithmException, InvalidKeyException;

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER', 'SCOPE_PERM_SALES')")
    void changeOrderStatus(Long orderId, OrderStatusEnum status);

    void doChangeOrderStatusToAwaiting(Long orderId);

    void removeOrder();

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER', 'SCOPE_PERM_SALES')")
    OrderResponse getOrderById(Long orderId);

    List<OrderStatusEnum> getAllOrderStatus();

    List<PaymentMethodEnum> getPaymentMethod();

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    PageWrapper<OrderResponse> getAllOrdersByUserId(OrderStatusEnum status,
                                                    int page, int size);

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    PaymentResponse getPaymentInfo(Long orderId) throws NoSuchAlgorithmException, InvalidKeyException;

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    PaymentResponse changePaymentMethod(Long orderId, PaymentMethodEnum paymentMethodEnum) throws NoSuchAlgorithmException, InvalidKeyException;

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SALES')")
    PageWrapper<OrderListResponse> getAllOrders(int page, int size, OrderStatusEnum statusEnum, String keyword, List<OrderStatusEnum> statuses);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER')")
    DashboardSummaryProjection getDashboardSummary();

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER')")
    List<DashboardCustomerTopProjection> getDashboardCustomerTop(int limit);

//    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER')")
//    List<DashboardRevenueResponse> getDashboardRevenue(LocalDate from, LocalDate to);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER')")
    List<DashboardRevenueProjection> getDashboardRevenue(int year);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER')")
    List<DashboardTopProductProjection> getDashboardTopProducts(int limit);
}
