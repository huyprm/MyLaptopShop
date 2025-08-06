package com.ptithcm2021.laptopshop.listener;

import com.ptithcm2021.laptopshop.event.CreateUserPromotionEvent;
import com.ptithcm2021.laptopshop.service.PromotionService;
import com.ptithcm2021.laptopshop.service.impl.PromotionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserPromotionListener {
    private final PromotionServiceImpl promotionService;
    @Async
    @EventListener
    public void createUserPromotion(CreateUserPromotionEvent event) {
        promotionService.handleEventCreateUserPromotion(event.getUserIds(), event.getPromotion());
    }
}
