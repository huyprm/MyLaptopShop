package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.DeliveryNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.enums.DeliveryNoteStatus;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SALES')")
public interface DeliveryNoteService {
    DeliveryNoteResponse addDeliveryNote(DeliveryNoteRequest deliveryNoteRequest);

    @Transactional
    DeliveryNoteResponse updateDeliveryNote(long id, DeliveryNoteRequest request);

    @Transactional
    void confirmDeliveryNote(long id);

    PageWrapper<DeliveryNoteResponse> getDeliveryNotesByOrderId(int page, int size, long orderId);

    PageWrapper<DeliveryNoteListResponse> getDeliveryNotesByCode(int page, int size, String orderCode, DeliveryNoteStatus status);

    DeliveryNoteResponse getDeliveryNoteById(long id);

    void deleteDeliveryNote(long id);
}
