package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.PurchaseOrderMapper;
import com.ptithcm2021.laptopshop.model.dto.request.PurChaseOrderRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder.PurchaseOrderListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder.PurchaseOrderResponse;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.PurchaseOrderService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ProductDetailRepository productDetailRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public PurchaseOrderResponse createPurchaseOrder(PurChaseOrderRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));

        String userId = FetchUserIdUtil.fetchUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .code(generateCode("PO"))
                .supplier(supplier)
                .userOrder(user)
                .status(PurchaseOrderStatusEnum.PENDING)
                .note(request.getNote())
                .build();

        List<PurchaseOrderDetail> purchaseOrderDetails = createPurchaseOrderDetails(request.getDetails(), purchaseOrder);

        int totalQuantity = purchaseOrderDetails.stream()
                .mapToInt(PurchaseOrderDetail::getQuantity)
                .sum();

        purchaseOrder.setDetails(purchaseOrderDetails);
        purchaseOrder.setTotalQuantity(totalQuantity);


        return purchaseOrderMapper.toPurchaseOrderResponse(purchaseOrderRepository.save(purchaseOrder));
    }

    @Override
    public PurchaseOrderResponse getPurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

        return purchaseOrderMapper.toPurchaseOrderResponse(purchaseOrder);
    }

    @Override
    public PurchaseOrderResponse updatePurchaseOrder(Long id, PurChaseOrderRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
        if (purchaseOrder.getStatus() != PurchaseOrderStatusEnum.PENDING) {
            throw new AppException(ErrorCode.PURCHASE_ORDER_CANNOT_BE_UPDATED);
        }
        if (!Objects.equals(request.getSupplierId(), purchaseOrder.getSupplier().getId())) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
            purchaseOrder.setSupplier(supplier);
        }

        if (request.getNote() != null && !request.getNote().isEmpty()) {
            purchaseOrder.setNote(request.getNote());
        }

        purchaseOrder.getDetails().clear();

        purchaseOrder.setDetails(createPurchaseOrderDetails(request.getDetails(), purchaseOrder));
        purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toPurchaseOrderResponse(purchaseOrder);
    }

    @Override
    public void deletePurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
        if (purchaseOrder.getStatus() != PurchaseOrderStatusEnum.PENDING) {
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
        purchaseOrderRepository.delete(purchaseOrder);
    }

    @Override
    public PageWrapper<PurchaseOrderListResponse> getPurchaseOrders(int page, int size, PurchaseOrderStatusEnum statusEnum, String keyword) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        String wrappedKeyword = (keyword == null || keyword.isBlank()) ? null : "%" + keyword + "%";

        Page<PurchaseOrder> purchaseOrderPage = purchaseOrderRepository.findByStatusAndCodeContaining(statusEnum, wrappedKeyword, pageable);

        return PageWrapper.<PurchaseOrderListResponse>builder()
                .content(purchaseOrderPage.stream().map(purchaseOrderMapper::toPurchaseOrderListResponse).collect(Collectors.toList()))
                .totalPages(purchaseOrderPage.getTotalPages())
                .totalElements(purchaseOrderPage.getTotalElements())
                .pageNumber(purchaseOrderPage.getNumber())
                .pageSize(purchaseOrderPage.getSize())
                .build();
    }

    private String generateCode(String prefix) {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseCode = prefix + datePart;

        // Lấy số thứ tự cao nhất hôm nay
        int countToday = purchaseOrderRepository.countByCodeStartingWith(baseCode);
        int nextNumber = countToday + 1;

        return baseCode + "-" + String.format("%03d", nextNumber);
    }

    private List<PurchaseOrderDetail> createPurchaseOrderDetails(List<PurChaseOrderRequest.PurchaseOrderDetailRequest> details, PurchaseOrder purchaseOrder) {
        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        for (PurChaseOrderRequest.PurchaseOrderDetailRequest detailRequest : details) {
            ProductDetail productDetail = productDetailRepository.findById(detailRequest.getProductDetailId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            PurchaseOrderDetail purchaseOrderDetail = PurchaseOrderDetail.builder()
                    .purchaseOrder(purchaseOrder)
                    .quantity(detailRequest.getQuantity())
                    .unitCost(detailRequest.getPrice())
                    .productDetail(productDetail)
                    .build();

            purchaseOrderDetails.add(purchaseOrderDetail);
        }
        return purchaseOrderDetails;
    }

}
