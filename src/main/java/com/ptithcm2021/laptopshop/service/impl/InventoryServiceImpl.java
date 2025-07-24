package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.entity.Inventory;
import com.ptithcm2021.laptopshop.model.entity.InventoryTransaction;
import com.ptithcm2021.laptopshop.model.enums.TransactionTypeEnum;
import com.ptithcm2021.laptopshop.repository.InventoryRepository;
import com.ptithcm2021.laptopshop.repository.InventoryTransactionRepository;
import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import com.ptithcm2021.laptopshop.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductDetailRepository productDetailRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

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
    public void createInvetoryTransaction(String serialNumber, Long productDetailId, Long orderId) {
        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(TransactionTypeEnum.EXPORT)
                .serialNumber(serialNumber)
                .referenceID(orderId)
                .productDetail(productDetailRepository.findById(productDetailId)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)))
                .quantity(1) // Assuming quantity is 1 for each transaction
                .build();
        inventoryTransactionRepository.save(inventoryTransaction);
    }
}
