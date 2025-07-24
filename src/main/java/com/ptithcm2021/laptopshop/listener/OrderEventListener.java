package com.ptithcm2021.laptopshop.listener;

import com.ptithcm2021.laptopshop.event.PaymentSuccessEvent;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final OrderService orderService;
    @Async
    @EventListener
    public void handle(PaymentSuccessEvent event) {
        orderService.changeOrderStatus(event.getOrderId(), OrderStatusEnum.AWAITING);
    }
}
