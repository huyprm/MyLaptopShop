package com.ptithcm2021.laptopshop.scheduler;

import com.ptithcm2021.laptopshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderService orderService;
    private final TaskScheduler taskScheduler;

    @Scheduled(cron = "0 */30 * * * *")
    public void schedulerRemoveOrder() {
        try {
            orderService.removeOrder();
        } catch (Exception e) {
            log.error("Remove order fail", e);
        }
    }
}
