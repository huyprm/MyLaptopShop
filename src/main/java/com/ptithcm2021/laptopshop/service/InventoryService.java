package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.entity.Inventory;
import jakarta.transaction.Transactional;

import java.util.List;

public interface InventoryService {
    void decreaseInventory(Inventory inventory, int quantity);

    List<String> getSerialNumbersByProductDetailIdAndActive(Long productDetailId);

    // void createInvetoryTransaction(String serialNumber, Long productDetailId, Long orderId);
}
