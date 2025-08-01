package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.GoodsReceiptNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_WAREHOUSE')")
public interface GoodsReceiptNoteService {
    GoodsReceiptNoteResponse createGRNResponse(GoodsReceiptNoteRequest goodsReceiptNoteRequest);

    void removeGRN(Long grnId);

    GoodsReceiptNoteResponse getGRNByCode(String code);

    GoodsReceiptNoteResponse getGRNById(Long id);

    PageWrapper<GoodsReceiptNoteListResponse> getGRNListByPurchaseOrderCode(int page, int size, String purchaseOrderCode);

    PageWrapper<GoodsReceiptNoteResponse> getGRNListByPurchaseOrderId(int page, int size, long purchaseOrderId);
}
