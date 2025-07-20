package com.ptithcm2021.laptopshop.listener;

import com.ptithcm2021.laptopshop.event.OrderCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DiscountRewardListener {
    @Async
    @EventListener
    public void handle(OrderCompletedEvent event) {
//        if (event.getOrder().getTotal() > 1000000) {
//            discountService.giveDiscountToUser(event.getOrder().getUser());
//        }
    }
}
