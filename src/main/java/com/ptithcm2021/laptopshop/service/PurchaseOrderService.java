package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.PurChaseOrderRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder.PurchaseOrderListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder.PurchaseOrderResponse;
import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_WAREHOUSE')")
public interface PurchaseOrderService {

    PurchaseOrderResponse createPurchaseOrder(PurChaseOrderRequest request);

    PurchaseOrderResponse getPurchaseOrderById(Long id);

    PurchaseOrderResponse updatePurchaseOrder(Long id, PurChaseOrderRequest request);

    void deletePurchaseOrder(Long id);

    PageWrapper<PurchaseOrderListResponse> getPurchaseOrders(int page, int size, PurchaseOrderStatusEnum statusEnum, String keyword);
}
