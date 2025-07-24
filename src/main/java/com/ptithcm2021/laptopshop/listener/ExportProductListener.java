package com.ptithcm2021.laptopshop.listener;

import com.ptithcm2021.laptopshop.event.ExportOrderEvent;
import com.ptithcm2021.laptopshop.repository.ProductRepository;
import com.ptithcm2021.laptopshop.service.impl.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExportProductListener {
    private final InventoryServiceImpl inventoryService;
    @Async
    @EventListener
    public void handle(ExportOrderEvent event) {
        inventoryService.createInvetoryTransaction(
                event.getSerialNumber(),
                event.getProductDetailId(),
                event.getOrderId());
    }
}
