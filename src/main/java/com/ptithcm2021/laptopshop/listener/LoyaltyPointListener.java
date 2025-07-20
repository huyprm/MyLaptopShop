package com.ptithcm2021.laptopshop.listener;

import com.ptithcm2021.laptopshop.event.OrderCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyPointListener {
    @Async
    @EventListener
    public void handle(OrderCompletedEvent event) {
//        int point = event.getOrder().getTotal() / 10000; // ví dụ
//        loyaltyPointService.addPoints(event.getOrder().getUser(), point);
    }
}
