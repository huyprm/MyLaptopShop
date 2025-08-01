package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.DeliveryNoteMapper;
import com.ptithcm2021.laptopshop.model.dto.request.DeliveryNoteDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.request.DeliveryNoteRequest;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.DeliveryNoteStatus;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.DeliveryNoteService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryNoteServiceImpl implements DeliveryNoteService {
    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;
    private final DeliveryNoteRepository deliveryNoteRepository;
    private final SerialProductItemRepository serialProductItemRepository;
    private final UserRepository userRepository;
    private final DeliveryNoteMapper deliveryNoteMapper;

    @Override
    @Transactional
    public DeliveryNoteResponse addDeliveryNote(DeliveryNoteRequest request) {
        String userId = FetchUserIdUtil.fetchUserId();
        DeliveryNote deliveryNote = new DeliveryNote();

        Order order = orderRepository.findByCode(request.getOrderCode())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(order.getStatus() != OrderStatusEnum.PROCESSING){
            throw new AppException(ErrorCode.ORDER_INVALID_STATUS);
        }

        List<Long> productDetailIds = request.getDetails().stream()
                .map(DeliveryNoteDetailRequest::getProductDetailId)
                .distinct()
                .toList();

        List<OrderDetailId> orderDetailIds = productDetailIds.stream()
                .map(productDetailId -> new OrderDetailId(order.getId(), productDetailId))
                .toList();

        Map<Long, ProductDetail> productDetails = productDetailRepository.findAllById(productDetailIds)
                .stream().collect(Collectors.toMap(ProductDetail::getId, p -> p));

        Map<Long, OrderDetail> orderDetails = order.getOrderDetails()
                .stream().collect(Collectors.toMap(od -> od.getId().getProductDetailId(), od -> od));

        DeliveryRecord deliveryRecord = handleDeliveryNoteDetails(
                request.getDetails(),
                productDetails,
                orderDetails,
                deliveryNote,
                request.getStatus());

        if(request.getStatus() == DeliveryNoteStatus.COMPLETED) {
            if (deliveryRecord.total == order.getTotalQuantity()) {
                order.setStatus(OrderStatusEnum.DELIVERED);
            } else {
                order.setStatus(OrderStatusEnum.PARTIALLY_DELIVERED);
            }
        }

        deliveryNote.setCode(generateCode("DN"));
        deliveryNote.setOrder(order);
        deliveryNote.setDeliveryNoteDetails(deliveryRecord.deliveryNoteDetails);
        deliveryNote.setNote(request.getNote());
        deliveryNote.setTotalQuantity(deliveryRecord.total);
        deliveryNote.setStaff(user);

        return deliveryNoteMapper.toDeliveryNoteResponse(deliveryNoteRepository.save(deliveryNote));
    }

    @Transactional
    @Override
    public DeliveryNoteResponse updateDeliveryNote(long id, DeliveryNoteRequest request) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOTE_NOT_FOUND));

        if (deliveryNote.getStatus() != DeliveryNoteStatus.DRAFT) {
            throw new AppException(ErrorCode.DELIVERY_CAN_NOT_BE_UPDATED);
        }

        Order order = orderRepository.findByCode(request.getOrderCode())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        List<Long> productDetailIds = request.getDetails().stream()
                .map(DeliveryNoteDetailRequest::getProductDetailId)
                .distinct()
                .toList();

        Map<Long, ProductDetail> productDetails = productDetailRepository.findAllById(productDetailIds)
                .stream().collect(Collectors.toMap(ProductDetail::getId, p -> p));

        Map<Long, OrderDetail> orderDetails = order.getOrderDetails()
                .stream().collect(Collectors.toMap(od -> od.getId().getProductDetailId(), od -> od));


        DeliveryRecord deliveryRecord = handleDeliveryNoteDetails(
                request.getDetails(),
                productDetails,
                orderDetails,
                deliveryNote,
                request.getStatus());

        if (request.getStatus() == DeliveryNoteStatus.COMPLETED) {
            if (deliveryRecord.total == order.getTotalQuantity()) {
                order.setStatus(OrderStatusEnum.DELIVERED);
            } else {
                order.setStatus(OrderStatusEnum.PARTIALLY_DELIVERED);
            }
        }

        deliveryNote.setCode(generateCode("DN"));
        deliveryNote.setOrder(order);

        deliveryNote.getDeliveryNoteDetails().clear();
        deliveryNote.setDeliveryNoteDetails(deliveryRecord.deliveryNoteDetails);

        deliveryNote.setNote(request.getNote());
        deliveryNote.setTotalQuantity(deliveryRecord.total);
        deliveryNote.setStatus(request.getStatus());

        return deliveryNoteMapper.toDeliveryNoteResponse(deliveryNoteRepository.save(deliveryNote));
    }

    @Transactional
    @Override
    public void confirmDeliveryNote(long id) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOTE_NOT_FOUND));

        if (deliveryNote.getStatus() != DeliveryNoteStatus.DRAFT) {
            throw new AppException(ErrorCode.DELIVERY_CAN_NOT_BE_UPDATED);
        }
        Order order = deliveryNote.getOrder();

        if (deliveryNote.getTotalQuantity() == order.getTotalQuantity()) {
            order.setStatus(OrderStatusEnum.DELIVERED);
        } else {
            order.setStatus(OrderStatusEnum.PARTIALLY_DELIVERED);
        }

        for (DeliveryNoteDetail detail : deliveryNote.getDeliveryNoteDetails()) {
            SerialProductItem serialProductItem = detail.getSerialNumber();
            if (!serialProductItem.isActive()) {
                throw new AppException(ErrorCode.SERIAL_NUMBER_SOLD);
            }

            serialProductItem.setActive(false);

        }

        deliveryNote.setStatus(DeliveryNoteStatus.COMPLETED);
        deliveryNote.setDate(LocalDate.now());
        deliveryNoteRepository.save(deliveryNote);
    }

    @Override
    public PageWrapper<DeliveryNoteResponse> getDeliveryNotesByOrderId(int page, int size, long orderId) {
        Page<DeliveryNote> deliveryNotes = deliveryNoteRepository.findByOrderId(orderId, PageRequest.of(page, size));
        return PageWrapper.<DeliveryNoteResponse>builder()
                .content(deliveryNotes.stream()
                        .map(deliveryNoteMapper::toDeliveryNoteResponse)
                        .collect(Collectors.toList()))
                .pageNumber(page)
                .pageSize(size)
                .totalPages(deliveryNotes.getTotalPages())
                .totalElements(deliveryNotes.getTotalElements())
                .build();
    }

    @Override
    public PageWrapper<DeliveryNoteListResponse> getDeliveryNotesByCode(int page, int size, String orderCode, DeliveryNoteStatus status) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        String wrappedOrderCode = (orderCode == null || orderCode.isBlank()) ? "" : '%'+orderCode+'%';
        Page<DeliveryNote> deliveryNotes = deliveryNoteRepository.findAllByOrderCodeAndStatus(wrappedOrderCode, status, pageRequest);

        List<DeliveryNoteListResponse> responses = deliveryNotes.stream()
                .map(deliveryNoteMapper::toDeliveryNoteListResponse)
                .collect(Collectors.toList());

        return PageWrapper.<DeliveryNoteListResponse>builder()
                .content(responses)
                .pageNumber(page)
                .pageSize(size)
                .totalPages(deliveryNotes.getTotalPages())
                .totalElements(deliveryNotes.getTotalElements())
                .build();
    }

    @Override
    public DeliveryNoteResponse getDeliveryNoteById(long id) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOTE_NOT_FOUND));

        return deliveryNoteMapper.toDeliveryNoteResponse(deliveryNote);
    }

    @Override
    public void deleteDeliveryNote(long id) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_NOTE_NOT_FOUND));

        if (deliveryNote.getStatus() != DeliveryNoteStatus.DRAFT) {
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }

        deliveryNoteRepository.delete(deliveryNote);
    }

    private DeliveryRecord handleDeliveryNoteDetails(List<DeliveryNoteDetailRequest> details,
                                                     Map<Long, ProductDetail> productDetails,
                                                     Map<Long, OrderDetail> orderDetails,
                                                     DeliveryNote deliveryNote,
                                                     DeliveryNoteStatus status) {
        int total = 0;
        List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<>();
        details.forEach(detail -> {
                    if (detail.getQuantity() != detail.getSerialProductItemId().size()){
                        throw new AppException(ErrorCode.SERIAL_NUMBER_QUANTITY_MISMATCH);
                    }

                    ProductDetail productDetail = productDetails.get(detail.getProductDetailId());
                    OrderDetail orderDetail = orderDetails.get(detail.getProductDetailId());

                    if (productDetail == null) {
                        throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
                    }
                    if (orderDetail == null) {
                        throw new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND);
                    }

                    int exported = deliveryNoteRepository.sumCountByOrderDetailId(productDetail.getId(), orderDetail.getId().getOrderId());
                    if (detail.getQuantity() + exported > orderDetail.getQuantity()) {
                        throw new AppException(ErrorCode.QUANTITY_EXCEED_ORDER);
                    }

                    detail.getSerialProductItemId().forEach(itemId -> {

                        SerialProductItem serialProductItem = serialProductItemRepository.findBySerialNumber(itemId)
                                .orElseThrow(() -> new AppException(ErrorCode.SERIAL_NUMBER_NOT_FOUND));

                        if (!serialProductItem.getProductDetail().getId().equals(productDetail.getId())) {
                            throw new AppException(ErrorCode.SERIAL_NUMBER_NOT_BELONGS_TO_PRODUCT);
                        }

                        if (!serialProductItem.isActive()) {
                            throw new AppException(ErrorCode.SERIAL_NUMBER_SOLD);
                        }

                        if( status == DeliveryNoteStatus.COMPLETED) {
                            serialProductItem.setActive(false);
                            productDetail.setSoldQuantity(productDetail.getSoldQuantity() + 1);
                        }

                        DeliveryNoteDetail deliveryNoteDetail = DeliveryNoteDetail.builder()
                                .deliveryNote(deliveryNote)
                                .productDetail(productDetail)
                                .quantity(1)
                                .serialNumber(serialProductItem)
                                .build();

                        deliveryNoteDetails.add(deliveryNoteDetail);
                    });
                });

        total = details.stream().mapToInt(DeliveryNoteDetailRequest::getQuantity).sum();

        return new DeliveryRecord(total, deliveryNoteDetails);
    }

    private record DeliveryRecord (int total, List<DeliveryNoteDetail> deliveryNoteDetails){};

    private String generateCode(String prefix) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseCode = prefix + currentDate;

        int count = deliveryNoteRepository.countByCodeStartingWith(baseCode);
         int nextCount = count + 1;

        return baseCode +'-' + String.format("%03d", nextCount);
    }

}
