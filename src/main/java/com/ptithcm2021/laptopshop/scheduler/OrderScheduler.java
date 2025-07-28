package com.ptithcm2021.laptopshop.scheduler;

import com.ptithcm2021.laptopshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class OrderScheduler {
    private final OrderService orderService;
    private final TaskScheduler taskScheduler;

    @Scheduled(cron = "0 */30 * * * *")
    public void schedulerRemoveOrder() {
        try {
            orderService.removeOrder();
            log.info("Remove order success at {}", Instant.now().truncatedTo(ChronoUnit.SECONDS));
        } catch (Exception e) {
            log.error("Remove order fail", e);
        }
    }
}
