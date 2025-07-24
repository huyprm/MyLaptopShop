package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.entity.Inventory;
import jakarta.transaction.Transactional;

public interface InventoryService {
    void decreaseInventory(Inventory inventory, int quantity);

    void createInvetoryTransaction(String serialNumber, Long productDetailId, Long orderId);
}
