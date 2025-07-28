package com.ptithcm2021.laptopshop.scheduler;

import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateDiscountPriceScheduler {

    private final ProductDetailRepository productDetailRepository;

    @Scheduled(cron = "0 */30 * * * *")
    public void updateDiscountPrice() {
        log.info("Calling stored procedure: updateDiscountPrice()");
        productDetailRepository.updateDiscountPrice();
        log.info("Stored procedure updateDiscountPrice() finished.");
    }
}
