package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.entity.Inventory;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface InventoryService {
    void decreaseInventory(Inventory inventory, int quantity);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SALES')")
    List<String> getSerialNumbersByProductDetailIdAndActive(Long productDetailId);

}
