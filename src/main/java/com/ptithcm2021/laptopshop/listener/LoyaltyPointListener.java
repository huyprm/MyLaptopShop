package com.ptithcm2021.laptopshop.listener;

import com.ptithcm2021.laptopshop.event.OrderCompletedEvent;
import com.ptithcm2021.laptopshop.event.PaymentSuccessEvent;
import com.ptithcm2021.laptopshop.service.RankLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoyaltyPointListener {
    private final RankLevelService rankLevelService;
    @Async
    @EventListener
    public void handle(OrderCompletedEvent event) {
        rankLevelService.loyaltyPointUpdate(event.getUserId(), event.getAmount());
    }
}
