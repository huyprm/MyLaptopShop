package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.OrderRequest;
import com.ptithcm2021.laptopshop.model.dto.response.OrderResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest) throws NoSuchAlgorithmException, InvalidKeyException;

    void changeOrderStatus(Long orderId, OrderStatusEnum status);

    //void changeOrderStatusToShipping(Long orderId, Map<Long, List<String>> serialNumbers);

    void removeOrder();

    OrderResponse getOrderById(Long orderId);

    List<OrderStatusEnum> getAllOrderStatus();

    List<PaymentMethodEnum> getPaymentMethod();

    PageWrapper<OrderResponse> getAllOrdersByUserId(OrderStatusEnum status,
                                                    int page, int size);

    PaymentResponse getPaymentInfo(Long orderId) throws NoSuchAlgorithmException, InvalidKeyException;

    PaymentResponse changePaymentMethod(Long orderId, PaymentMethodEnum paymentMethodEnum) throws NoSuchAlgorithmException, InvalidKeyException;
}
