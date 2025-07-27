package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.entity.Inventory;
import com.ptithcm2021.laptopshop.repository.InventoryRepository;
import com.ptithcm2021.laptopshop.repository.SerialProductItemRepository;
import com.ptithcm2021.laptopshop.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final SerialProductItemRepository seriesRepository;

    @Override
    @Transactional
    public void decreaseInventory(Inventory inventory, int quantity) {

        if (inventory.getQuantity() < quantity) {
            throw new AppException(ErrorCode.PRODUCT_IS_OUT_OF_QUANTITY);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    public List<String> getSerialNumbersByProductDetailIdAndActive(Long productDetailId) {
        return seriesRepository.findAllSerialNumbersByProductDetailIdAndActive(productDetailId);
    }
}
