package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.GoodsReceiptNoteMapper;
import com.ptithcm2021.laptopshop.model.dto.request.GoodsReceiptNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.GoodsReceiptNoteService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class GoodsReceiptNoteServiceImp implements GoodsReceiptNoteService {
    private final GoodsReceiptNoteRepository goodsReceiptNoteRepository;
    private final UserRepository userRepository;
    private final ProductDetailRepository productDetailRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final GoodsReceiptNoteMapper goodsReceiptNoteMapper;
    private final SerialProductItemRepository serialProductItemRepository;

    @Override
    @Transactional
    public GoodsReceiptNoteResponse createGRNResponse(GoodsReceiptNoteRequest request) {
        String userId = FetchUserIdUtil.fetchUserId();

        User staff = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByCode(request.getPurchaseOrderCode())
                .orElseThrow(() -> new AppException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

        if (purchaseOrder.getStatus() == PurchaseOrderStatusEnum.COMPLETED) {
            throw new AppException(ErrorCode.PURCHASE_ORDER_COMPLETED);
        }

        // Fetch all necessary ProductDetail and PurchaseOrderDetail in batch
        List<Long> productDetailIds = request.getDetailRequestList().stream()
                .map(GoodsReceiptNoteRequest.GoodsReceiptNoteDetailRequest::getProductDetailId)
                .distinct()
                .toList();

        List<Long> poDetailIds = request.getDetailRequestList().stream()
                .map(GoodsReceiptNoteRequest.GoodsReceiptNoteDetailRequest::getPurchaseOrderDetailId)
                .distinct()
                .toList();

        Map<Long, ProductDetail> productDetails = productDetailRepository.findAllById(productDetailIds).stream()
                .collect(Collectors.toMap(ProductDetail::getId, Function.identity()));

        Map<Long, PurchaseOrderDetail> poDetails = purchaseOrderDetailRepository.findAllById(poDetailIds).stream()
                .collect(Collectors.toMap(PurchaseOrderDetail::getId, Function.identity()));

        // Validate & Create
        GoodsReceiptNote goodsReceiptNote = new GoodsReceiptNote();

        GRNrecord grnDetail = handleGRNDetail(request.getDetailRequestList(), goodsReceiptNote, purchaseOrder, productDetails, poDetails);

        int totalQuantity = grnDetail.totalQuantity();
        int totalReceived = goodsReceiptNoteRepository.sumReceivedByPurchaseOrderId(purchaseOrder.getId());

        if (totalReceived + totalQuantity < purchaseOrder.getTotalQuantity()) {
            purchaseOrder.setStatus(PurchaseOrderStatusEnum.PARTIALLY_RECEIVED);
        } else if (totalReceived + totalQuantity == purchaseOrder.getTotalQuantity()) {
            purchaseOrder.setStatus(PurchaseOrderStatusEnum.COMPLETED);
        } else {
            throw new AppException(ErrorCode.QUANTITY_EXCEED_PURCHASE_ORDER);
        }

        goodsReceiptNote.setPurchaseOrder(purchaseOrder);
        goodsReceiptNote.setStaff(staff);
        goodsReceiptNote.setCode(generateCode("GRN"));
        goodsReceiptNote.setReceivedDate(request.getReceivedDate());
        goodsReceiptNote.setNote(request.getNote());
        goodsReceiptNote.setGrnDetails(grnDetail.detail());
        goodsReceiptNote.setTotalQuantity(totalQuantity);

        GoodsReceiptNote saved = goodsReceiptNoteRepository.save(goodsReceiptNote);

        return goodsReceiptNoteMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void removeGRN(Long grnId) {
        GoodsReceiptNote goodsReceiptNote = goodsReceiptNoteRepository.findById(grnId)
                .orElseThrow(() -> new AppException(ErrorCode.GOODS_RECEIPT_NOTE_NOT_FOUND));
        for(GRNDetail detail : goodsReceiptNote.getGrnDetails()) {
            if (!detail.getSerialNumber().isActive()) {
                throw new AppException(ErrorCode.CANNOT_DELETE);
            }

            // Trả lại số lượng tồn kho
            ProductDetail productDetail = detail.getProductDetail();
            productDetail.getInventory().setQuantity(productDetail.getInventory().getQuantity() - detail.getQuantity());
        }
        PurchaseOrder purchaseOrder = goodsReceiptNote.getPurchaseOrder();
        if(purchaseOrder.getDetails().size() ==1) {
            purchaseOrder.setStatus(PurchaseOrderStatusEnum.PENDING);
        } else if( purchaseOrder.getStatus() != PurchaseOrderStatusEnum.PARTIALLY_RECEIVED) {
            purchaseOrder.setStatus(PurchaseOrderStatusEnum.PARTIALLY_RECEIVED);
        }
        goodsReceiptNoteRepository.delete(goodsReceiptNote);
    }

    @Override
    public GoodsReceiptNoteResponse getGRNByCode(String code) {
        GoodsReceiptNote goodsReceiptNote = goodsReceiptNoteRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.GOODS_RECEIPT_NOTE_NOT_FOUND));

        return goodsReceiptNoteMapper.toResponse(goodsReceiptNote);
    }

    @Override
    public GoodsReceiptNoteResponse getGRNById(Long id) {
        GoodsReceiptNote goodsReceiptNote = goodsReceiptNoteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GOODS_RECEIPT_NOTE_NOT_FOUND));

        return goodsReceiptNoteMapper.toResponse(goodsReceiptNote);
    }

    @Override
    public PageWrapper<GoodsReceiptNoteListResponse> getGRNListByPurchaseOrderCode(int page, int size, String grnCode) {
        String wrappedCode = (grnCode== null || grnCode.isBlank()) ? null : '%'+grnCode+'%';

        PageRequest pageRequest = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "receivedDate"));

        Page<GoodsReceiptNote> grnList = goodsReceiptNoteRepository.findAllByGRNCode(wrappedCode, pageRequest);
        List<GoodsReceiptNoteListResponse> responses = grnList.stream()
                .map(goodsReceiptNoteMapper::toListResponse)
                .collect(Collectors.toList());

        return PageWrapper.<GoodsReceiptNoteListResponse>builder()
                .content(responses)
                .totalPages(grnList.getTotalPages())
                .totalElements(grnList.getTotalElements())
                .pageNumber(grnList.getNumber())
                .pageSize(grnList.getSize())
                .build();
    }

    @Override
    public PageWrapper<GoodsReceiptNoteResponse> getGRNListByPurchaseOrderId(int page, int size, long purchaseOrderId) {

        Page<GoodsReceiptNote> grnList = goodsReceiptNoteRepository.findAllByPurchaseOrderId(purchaseOrderId, PageRequest.of(page, size));
        List<GoodsReceiptNoteResponse> responses = grnList.stream()
                .map(goodsReceiptNoteMapper::toResponse)
                .collect(Collectors.toList());

        return PageWrapper.<GoodsReceiptNoteResponse>builder()
                .content(responses)
                .totalPages(grnList.getTotalPages())
                .totalElements(grnList.getTotalElements())
                .pageNumber(grnList.getNumber())
                .pageSize(grnList.getSize())
                .build();
    }

    private GRNrecord handleGRNDetail(List<GoodsReceiptNoteRequest.GoodsReceiptNoteDetailRequest> requests,
                                      GoodsReceiptNote goodsReceiptNote,
                                      PurchaseOrder purchaseOrder,
                                      Map<Long, ProductDetail> productDetails,
                                      Map<Long, PurchaseOrderDetail> poDetails) {
        int totalQuantity = 0;
        int totalPrice = 0;
        List<GRNDetail> details = new ArrayList<>();

        for (var req : requests) {
            // Nếu mở rộng cho sản phẩm không có serial number, cần bỏ qua phần kiểm tra serial number và increment quantity accordingly
            // Validate quantity
            if (req.getSerialNumber().size() != req.getQuantity()) {
                throw new AppException(ErrorCode.SERIAL_NUMBER_QUANTITY_MISMATCH);
            }

            ProductDetail productDetail = productDetails.get(req.getProductDetailId());
            PurchaseOrderDetail poDetail = poDetails.get(req.getPurchaseOrderDetailId());

            if (productDetail == null) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            if (poDetail == null) {
                throw new AppException(ErrorCode.PURCHASE_ORDER_DETAIL_NOT_FOUND);
            }

            // Check if PurchaseOrderDetail belongs to the PurchaseOrder
            if (!poDetail.getPurchaseOrder().getId().equals(purchaseOrder.getId())) {
                throw new AppException(ErrorCode.PURCHASE_ORDER_DETAIL_NOT_BELONG_TO_PURCHASE_ORDER);
            }

            // Check if ProductDetail matches the PurchaseOrderDetail
            if (!poDetail.getProductDetail().getId().equals(productDetail.getId())) {
                throw new AppException(ErrorCode.PRODUCT_DETAIL_NOT_MATCH);
            }

            int alreadyReceived = goodsReceiptNoteRepository.sumReceivedByPurchaseOrderDetailId(req.getPurchaseOrderDetailId());
            if (req.getQuantity() + alreadyReceived > poDetail.getQuantity()) {
                throw new AppException(ErrorCode.QUANTITY_EXCEED_PURCHASE_ORDER_DETAIL);
            }

            for (String serial : req.getSerialNumber()) {
                if(serialProductItemRepository.existsBySerialNumber(serial)) {
                    throw new AppException(ErrorCode.SERIAL_NUMBER_ALREADY_EXISTS);
                }

                SerialProductItem serialProductItem = SerialProductItem.builder()
                        .productDetail(productDetail)
                        .serialNumber(serial)
                        .build();

                GRNDetail grnDetail = GRNDetail.builder()
                        .goodsReceiptNote(goodsReceiptNote)
                        .productDetail(productDetail)
                        .purchaseOrderDetail(poDetail)
                        .serialNumber(serialProductItem)
                        .quantity(1) // Nếu 1 serial = 1 sản phẩm
                        .unitPrice(req.getUnitPrice())
                        .build();

                serialProductItem.setGrnDetail(grnDetail);

                details.add(grnDetail);
            }

            productDetail.getInventory().setQuantity(productDetail.getInventory().getQuantity() + req.getQuantity());

            totalQuantity += req.getQuantity();
            totalPrice += req.getQuantity() * req.getUnitPrice();
        }

        return new GRNrecord(details, totalQuantity, totalPrice);
    }


    private String generateCode(String prefix) {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseCode = prefix + datePart;

        int countToday = goodsReceiptNoteRepository.countByCodeStartingWith(baseCode);
        int nextNumber = countToday + 1;

        return baseCode + "-" + String.format("%03d", nextNumber);
    }

    private record GRNrecord(List<GRNDetail> detail, Integer totalQuantity, Integer totalPrice){};
}
