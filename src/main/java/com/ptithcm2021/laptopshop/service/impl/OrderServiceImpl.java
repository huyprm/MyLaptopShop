package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.OrderMapper;
import com.ptithcm2021.laptopshop.model.dto.request.OrderRequest;
import com.ptithcm2021.laptopshop.model.dto.request.PaymentRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Order.OderDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Order.OrderListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Order.OrderResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PaymentResponse;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.CacheService;
import com.ptithcm2021.laptopshop.service.InventoryService;
import com.ptithcm2021.laptopshop.service.OrderService;
import com.ptithcm2021.laptopshop.service.PromotionService;
import com.ptithcm2021.laptopshop.strategy.PaymentStrategyFactory;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductDetailRepository productDetailRepository;
    private final InventoryRepository inventoryRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PromotionService promotionService;
    private final InventoryService inventoryService;
    private final OrderMapper orderMapper;
    private final CacheService cacheService;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        if (orderRequest.getUserId() == null || orderRequest.getUserId().isEmpty()) {
            String userId = FetchUserIdUtil.fetchUserId();
            orderRequest.setUserId(userId);
        }

        Order order = new Order();
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setNote(orderRequest.getNote());
        order.setCode(generateCode("OD"));

        if (orderRequest.getPaymentMethod() == PaymentMethodEnum.COD)
            order.setStatus(OrderStatusEnum.AWAITING);
        else
            order.setStatus(OrderStatusEnum.PENDING);

        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        order.setUser(user);

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.ADDRESS_NOT_BELONG_TO_USER);
        }
        order.setAddress(address.getAddress());
        order.setPhone(address.getPhone());
        order.setRecipient(address.getRecipient());

        OrderDetailRecord unit = handelOrderDetail(orderRequest.getDetailRequest(), order);

        int userDiscount = promotionService.applyPromotion(
                orderRequest.getUserPromotionId(),
                orderRequest.getUserId(),
                unit.totalPrice(),
                order::setUserDiscount,
                null
        );

        int shopDiscount = promotionService.applyPromotion(
                orderRequest.getShopPromotionId(),
                orderRequest.getUserId(),
                unit.totalPrice(),
                order::setShopDiscount,
                null
        );

        int totalPrice = unit.totalPrice - shopDiscount - userDiscount;

        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(unit.totalQuantity);

        orderRepository.save(order);

        OrderResponse response = mapToOrderResponse(order);

        if (!orderRequest.getPaymentMethod().equals(PaymentMethodEnum.COD)){
            PaymentResponse paymentResponse = handlePayment(order.getId(), orderRequest.getPaymentMethod(), totalPrice, user.getId());
            response.setPayUrl(paymentResponse.getPayUrl());
        }

        return response;
    }

    @Transactional
    @Override
    public void changeOrderStatus(Long orderId, OrderStatusEnum status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatusEnum.COMPLETED || order.getStatus() == OrderStatusEnum.CANCELED) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CHANGED);
        }

        if( order.getStatus().getPriority() > status.getPriority()) {
            throw new AppException(ErrorCode.STATUS_INVALID);
        }

        if(status == OrderStatusEnum.CANCELED) {
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                inventoryRepository.increaseQuantity(orderDetail.getProductDetail().getId(), orderDetail.getQuantity());
                cacheService.evictSingleProduct(orderDetail.getProductDetail().getId());
            }
        }

        if (status == OrderStatusEnum.PARTIALLY_DELIVERED || status == OrderStatusEnum.DELIVERED){
            throw new AppException(ErrorCode.API_CANNOT_CHANGE_TO_SHIPPING);
        }
        order.setStatus(status);
        orderRepository.save(order);
    }


    @Transactional
    @Override
    public void removeOrder() {
        orderRepository.removeExpiredOrders();
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return mapToOrderResponse(order);
    }

    @Override
    public List<OrderStatusEnum> getAllOrderStatus() {
        return Arrays.stream(OrderStatusEnum.values())
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentMethodEnum> getPaymentMethod() {
        return Arrays.stream(PaymentMethodEnum.values())
                .collect(Collectors.toList());
    }


    @Override
    public PageWrapper<OrderResponse> getAllOrdersByUserId(OrderStatusEnum status,
                                                           int page, int size) {
        String userId = FetchUserIdUtil.fetchUserId();

        Pageable pageable = PageRequest.of(page, size);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (status == null){
            Page<Order> orders = orderRepository.findAllByUser(user, pageable);
            return PageWrapper.<OrderResponse>builder()
                    .content(orders.getContent().stream()
                            .map(this::mapToOrderResponse)
                            .collect(Collectors.toList()))
                    .totalElements(orders.getTotalElements())
                    .totalPages(orders.getTotalPages())
                    .build();
        }

        Page<Order> orders = orderRepository.findAllByUserAndStatus(user, status, pageable);

        return PageWrapper.<OrderResponse>builder()
                .content(orders.getContent().stream()
                        .map(this::mapToOrderResponse)
                        .collect(Collectors.toList()))
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
    }

    @Override
    public PaymentResponse getPaymentInfo(Long orderId) throws NoSuchAlgorithmException, InvalidKeyException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if( order.getStatus() != OrderStatusEnum.PENDING) {
            throw new AppException(ErrorCode.CANNOT_PAYMENT);
        }

        return handlePayment(orderId, order.getPaymentMethod(), order.getTotalPrice(), order.getUser().getId());
    }

    @Override
    public PaymentResponse changePaymentMethod(Long orderId, PaymentMethodEnum paymentMethodEnum) throws NoSuchAlgorithmException, InvalidKeyException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() != OrderStatusEnum.PENDING) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CHANGED);
        }

        order.setPaymentMethod(paymentMethodEnum);

        orderRepository.save(order);

        return handlePayment(orderId, order.getPaymentMethod(), order.getTotalPrice(), order.getUser().getId());
    }

    @Override
    public PageWrapper<OrderListResponse> getAllOrders(int page, int size, OrderStatusEnum statusEnum, String keyword) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "createdDate"));

        String wrappedKeyword = keyword != null && !keyword.isBlank() ? "%" + keyword + "%" : null;

        Page<Order> orders = orderRepository.findByCodeAndStatus(wrappedKeyword, statusEnum, pageable);
        return PageWrapper.<OrderListResponse>builder()
                .content(orders.stream().map(orderMapper::toOrderListResponse).toList())
                .pageSize(orders.getSize())
                .pageNumber(orders.getNumber())
                .totalPages(orders.getTotalPages())
                .totalElements(orders.getTotalElements())
                .build();
    }

    private OrderDetailRecord handelOrderDetail(List<OrderRequest.OrderDetailRequest> detailRequests, Order order) {
        int totalPrice = 0;
        int totalQuantity = 0;
        for (OrderRequest.OrderDetailRequest detailRequest : detailRequests) {

            Inventory inventory = inventoryRepository.findInventoryForUpdate(detailRequest.getProductDetailId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            ProductDetail productDetail = inventory.getProductDetail();

            inventoryService.decreaseInventory(inventory, detailRequest.getQuantity());

            int discount = promotionService.applyPromotion(
                    detailRequest.getProductPromotionId(),
                    order.getUser().getId(),
                    productDetail.getOriginalPrice(),
                    null,
                    productDetail
            );

            int price = (productDetail.getOriginalPrice() - discount) * detailRequest.getQuantity();

            OrderDetailId orderDetailId = OrderDetailId.builder()
                    .orderId(order.getId())
                    .productDetailId(productDetail.getId())
                    .build();

            OrderDetail orderDetail = OrderDetail.builder()
                    .id(orderDetailId)
                    .order(order)
                    .productDetail(productDetail)
                    .productPromotionId(detailRequest.getProductPromotionId() != null ? detailRequest.getProductPromotionId() : null)
                    .quantity(detailRequest.getQuantity())
                    .originalPrice(productDetail.getOriginalPrice())
                    .price(productDetail.getOriginalPrice() - discount)
                    .build();

            order.getOrderDetails().add(orderDetail);

            totalPrice += price;
            totalQuantity += detailRequest.getQuantity();

            cacheService.evictSingleProduct(productDetail.getId());
        }
        return new OrderDetailRecord(totalPrice, totalQuantity);
    }

    private record OrderDetailRecord(int totalPrice, int totalQuantity) {}

    private OrderResponse mapToOrderResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .address(order.getAddress())
                .phone(order.getPhone())
                .code(order.getCode())
                .recipient(order.getRecipient())
                .userId(order.getUser().getId())
                .status(order.getStatus())
                .note(order.getNote())
                .createdAt(order.getCreatedDate())
                .userDiscount(order.getUserDiscount())
                .shopDiscount(order.getShopDiscount())
                .paymentMethod(order.getPaymentMethod())
                .totalPrice(order.getTotalPrice())
                .totalQuantity(order.getTotalQuantity())
                .orderDetails(order.getOrderDetails().stream()
                        .map(detail -> OderDetailResponse.builder()
                                .colorName(detail.getProductDetail().getColor().getName())
                                .hardDriveValue(detail.getProductDetail().getConfig().getHardDriveValue())
                                .ramValue(detail.getProductDetail().getConfig().getRamValue())
                                .title(detail.getProductDetail().getTitle())
                                .thumbnail(detail.getProductDetail().getThumbnail())
                                .productDetailId(detail.getProductDetail().getId())
                                .quantity(detail.getQuantity())
                                .originalPrice(detail.getOriginalPrice())
                                .price(detail.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private PaymentResponse handlePayment(Long orderId, PaymentMethodEnum paymentMethod, int totalAmount, String userId) throws NoSuchAlgorithmException, InvalidKeyException {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderId)
                .paymentMethod(paymentMethod)
                .amount(totalAmount)
                .userId(userId)
                .build();

        return paymentStrategyFactory.getPaymentStrategy(paymentMethod)
                .pay(paymentRequest);
    }

    private String generateCode(String prefix) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseCode = prefix + currentDate;

        int count = orderRepository.countByCodeStartingWith(baseCode);
        int nextCount = count + 1;

        return baseCode +'-' + String.format("%03d", nextCount);
    }

}
