package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.DeliveryNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SALES')")
public interface DeliveryNoteService {
    DeliveryNoteResponse addDeliveryNote(DeliveryNoteRequest deliveryNoteRequest);

    @Transactional
    DeliveryNoteResponse updateDeliveryNote(long id, DeliveryNoteRequest request);

    @Transactional
    void confirmDeliveryNote(long id);

    PageWrapper<DeliveryNoteResponse> getDeliveryNotes(int page, int size);

    PageWrapper<DeliveryNoteResponse> getDeliveryNotesByProductDetailId(int page, int size, String orderCode);

    DeliveryNoteResponse getDeliveryNoteById(long id);

    void deleteDeliveryNote(long id);
}
