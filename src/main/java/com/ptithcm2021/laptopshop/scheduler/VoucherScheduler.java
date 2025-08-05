package com.ptithcm2021.laptopshop.scheduler;

import com.ptithcm2021.laptopshop.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoucherScheduler {
    private final PromotionService promotionService;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredOrUsedVoucher() {
        promotionService.handlePromotionUserExpiredOrUsage();
    }
}
