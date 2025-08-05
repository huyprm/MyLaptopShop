package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.PromotionMapper;
import com.ptithcm2021.laptopshop.model.dto.projection.VoucherProjection;
import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.PromotionResponse;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import com.ptithcm2021.laptopshop.repository.PromotionRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.PromotionService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;
    private final RedissonClient redissonClient;

    @Override
    public PromotionResponse addPromotion(PromotionRequest request) {
        if (request.getEndDate() != null &&
                request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        String userId = FetchUserIdUtil.fetchUserId();
        User userCreate = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Promotion promotion = promotionMapper.toPromotion(request);

        if (request.getProductDetailIds() != null && !request.getProductDetailIds().isEmpty()
                && request.getPromotionType() == PromotionTypeEnum.PRODUCT_DISCOUNT) {
            handleProductPromotions(request.getProductDetailIds(), promotion);
        }

        if( request.getPromotionType() == PromotionTypeEnum.USER_PROMOTION
                && request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            handleUserPromotion(request.getUserIds(), promotion);
        }

        promotion.setUserCreate(userCreate);
        return promotionMapper.toPromotionResponse(promotionRepository.save(promotion));
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(PromotionRequest request, long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        promotionMapper.updatePromotion(request,promotion);

        validatePromotionDate(LocalDateTime.now(), promotion);


        if (request.getProductDetailIds() != null && !request.getProductDetailIds().isEmpty()
                && request.getPromotionType() == PromotionTypeEnum.PRODUCT_DISCOUNT) {
            handleProductPromotions(request.getProductDetailIds(), promotion);
        }

        if( request.getPromotionType() == PromotionTypeEnum.USER_PROMOTION
                && request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            handleUserPromotion(request.getUserIds(), promotion);
        }

        return promotionMapper.toPromotionResponse(promotionRepository.save(promotion));
    }

    @Override
    public void deletePromotion(long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        validatePromotionDate(LocalDateTime.now(), promotion);

        try{
            promotionRepository.delete(promotion);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    public PromotionResponse getPromotion(long id) {
        return promotionMapper.toPromotionResponse(promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND)));
    }

    @Override
    public PageWrapper<PromotionResponse> getPromotions(PromotionTypeEnum promotionType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (promotionType == null) {
             Page<Promotion> promotion = promotionRepository.findAll(pageable);
             return PageWrapper.<PromotionResponse>builder()
                     .content(promotion.getContent().stream()
                             .map(promotionMapper::toPromotionResponse)
                             .toList())
                        .pageNumber(promotion.getNumber())
                        .pageSize(promotion.getSize())
                        .totalElements(promotion.getTotalElements())
                        .totalPages(promotion.getTotalPages())
                     .build();
        }

        Page<Promotion> promotion = promotionRepository.findAllByPromotionType(promotionType,pageable);
        return PageWrapper.<PromotionResponse>builder()
                .content(promotion.getContent().stream()
                        .map(promotionMapper::toPromotionResponse)
                        .toList())
                .pageNumber(promotion.getNumber())
                .pageSize(promotion.getSize())
                .totalElements(promotion.getTotalElements())
                .totalPages(promotion.getTotalPages())
                .build();
    }


    @Override
    public List<PromotionTypeEnum> getPromotionTypes() {
        return Arrays.stream(PromotionTypeEnum.values()).toList();
    }

    @Override
    public void collectPromotion(long id) {
        String userId = FetchUserIdUtil.fetchUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        if (!promotion.getPromotionType().equals(PromotionTypeEnum.USER_PROMOTION)) {
            throw new AppException(ErrorCode.CAN_NOT_COLLECTED);
        }

        if (promotion.getEndDate() != null && LocalDateTime.now().isAfter(promotion.getEndDate())) {
            throw new AppException(ErrorCode.PROMOTION_IS_EXPIRED);
        }

        UserPromotion userPromotion = UserPromotion.builder()
                .user(user)
                .promotion(promotion)
                .build();
        user.getVoucher().add(userPromotion);

        userRepository.save(user);
    }

    @Override
    public List<VoucherProjection> myVouchers() {
        String userId = FetchUserIdUtil.fetchUserId();
        List<VoucherProjection> voucherProjections = promotionRepository.findUserVouchers(userId);
        for (VoucherProjection v : voucherProjections) {
            log.info("Voucher: code = {}, used = {}, usedCount = {}", v.getCode(), v.getUsed(), v.getDiscountValue());
        }

        return voucherProjections;
    }

    @Override
    public List<PromotionResponse> getProductPromotions(long id, PromotionStatusEnum statusEnum) {
        if (statusEnum == PromotionStatusEnum.ACTIVE)
            return promotionRepository.findProductPromotionsIsActive(id, LocalDateTime.now())
                    .stream()
                    .map(promotionMapper::toPromotionResponse)
                    .toList();

        if (statusEnum == PromotionStatusEnum.EXPIRED)
            return promotionRepository.findProductPromotionsIsExpired(id, LocalDateTime.now())
                    .stream()
                    .map(promotionMapper::toPromotionResponse)
                    .toList();

        if  ( statusEnum == PromotionStatusEnum.INACTIVE)
            return promotionRepository.findUpcomingPromotionsByProductDetailId(id, LocalDateTime.now())
                    .stream()
                    .map(promotionMapper::toPromotionResponse)
                    .toList();

        return promotionRepository.findProductPromotions(id)
                .stream()
                .map(promotionMapper::toPromotionResponse)
                .toList();
    }

    private void validatePromotionDate(LocalDateTime now, Promotion promotion) {
        if (promotion.getEndDate() != null && now.isBefore(promotion.getEndDate()) && now.isAfter(promotion.getStartDate())) {
            throw new AppException(ErrorCode.PROMOTION_IS_ACTIVE);
        }
        if (promotion.getEndDate() != null && now.isAfter(promotion.getEndDate())) {
            throw new AppException(ErrorCode.PROMOTION_IS_EXPIRED);
        }
        if (promotion.getEndDate() == null && now.isAfter(promotion.getStartDate())) {
            throw new AppException(ErrorCode.PROMOTION_IS_ACTIVE);
        }

    }

    private void handleProductPromotions(List<Long> productDetailIds, Promotion promotion) {
        if (promotion.getProductPromotions() == null) {
            promotion.setProductPromotions(new ArrayList<>());
        }

        List<ProductPromotion> currentProductPromotions = promotion.getProductPromotions();

        Set<Long> newProductIdSet = new HashSet<>(Optional.ofNullable(productDetailIds).orElse(Collections.emptyList()));

        // XÓA các productDetailId không còn trong danh sách mới
        currentProductPromotions.removeIf(pp ->
                pp.getProductDetail() != null && !newProductIdSet.contains(pp.getProductDetail().getId())
        );

        // Lấy set productId hiện tại sau khi xóa
        Set<Long> currentAfterRemove = currentProductPromotions.stream()
                .map(pp -> pp.getProductDetail() != null ? pp.getProductDetail().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> toAdd = new HashSet<>(newProductIdSet);
        toAdd.removeAll(currentAfterRemove); // Chỉ còn productId mới

        if (toAdd.isEmpty() && newProductIdSet.isEmpty()) {
            // Trường hợp 1: danh sách trống và hiện tại cũng trống → giữ nguyên
            return;
        }

        if (newProductIdSet.isEmpty()) {
            // Trường hợp 2: null hoặc empty → dành cho tất cả sản phẩm
            currentProductPromotions.clear();
            currentProductPromotions.add(ProductPromotion.builder()
                    .promotion(promotion)
                    .build());
            return;
        }

        // Trường hợp 3: có danh sách mới → thêm vào
        List<ProductDetail> products = productDetailRepository.findAllById(toAdd);
        if (products.size() != toAdd.size()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        products.forEach(product -> {
            ProductPromotion productPromotion = ProductPromotion.builder()
                    .productDetail(product)
                    .promotion(promotion)
                    .build();
            currentProductPromotions.add(productPromotion);
        });
    }


    @Override
    @Transactional
    public int applyPromotion(Long promotionId, String userId, int totalAmount, Consumer<Integer> setDiscountFn, @Nullable ProductDetail productDetail) {
        if (promotionId == null) return 0;

        RLock lock = redissonClient.getLock("promotion:" + promotionId);
        try{
            lock.lock(5, TimeUnit.SECONDS);


            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));


            if (promotion.getPromotionType() == PromotionTypeEnum.PRODUCT_DISCOUNT ) {
                validatePromotionProduct(promotion, productDetail);

            } else
                validatePromotion(promotion, totalAmount, userId);


            int discount = calculateDiscount(totalAmount, promotion);

            if (setDiscountFn != null)
                setDiscountFn.accept(discount);

            promotion.setUsageCount(promotion.getUsageCount() + 1);

            promotionRepository.save(promotion);

            return discount;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional
    public void handlePromotionUserExpiredOrUsage() {
        promotionRepository.deleteUserPromotionExpiredOrUsed();
        log.info("Deleted expired or used user promotions");
    }

    private void validatePromotionProduct(Promotion promotion, ProductDetail productDetail) {
        LocalDateTime currentTime = LocalDateTime.now();

        boolean notYetStarted = promotion.getStartDate().isAfter(currentTime);
        boolean alreadyEnded = promotion.getEndDate() != null && promotion.getEndDate().isBefore(currentTime);

        if (notYetStarted || alreadyEnded) {
            log.warn("Promotion is not valid: {}", promotion.getId());
            throw new AppException(ErrorCode.PROMOTION_IS_EXPIRED);
        }

        if (promotion.getPromotionType() != PromotionTypeEnum.PRODUCT_DISCOUNT) {
            log.warn("Promotion is not valid: {}", promotion.getId());
            throw new AppException(ErrorCode.PROMOTION_IS_NOT_VALID);
        }

        Set<Long> promotionIds = new HashSet<>(promotionRepository.findAvailableProductPromotions(productDetail.getId(), currentTime));
        boolean isValid = promotionIds.contains(promotion.getId());
        if (!isValid) {
            throw new AppException(ErrorCode.PROMOTION_IS_NOT_VALID);
        }

        if (promotion.getUsageLimit() != null) {
            if (promotion.getUsageLimit() == promotion.getUsageCount() + 1) {
                log.warn("Promotion is not valid: {}", promotion.getId());
                throw new AppException(ErrorCode.PROMOTION_USAGE_LIMIT_EXCEEDED);
            }
        }
    }

    private void validatePromotion(Promotion promotion, int totalAmount, String userId) {
        LocalDateTime currentTime = LocalDateTime.now();

        // Check if the promotion is valid for the current time
        boolean notYetStarted = promotion.getStartDate().isAfter(currentTime);
        boolean alreadyEnded = promotion.getEndDate() != null && promotion.getEndDate().isBefore(currentTime);

        if (notYetStarted || alreadyEnded) {
            log.warn("Promotion is not valid: {}", promotion.getId());
            throw new AppException(ErrorCode.PROMOTION_IS_EXPIRED);
        }

        // Check if the promotion is already used up
        if (promotion.getUsageLimit() != null && promotion.getUsageLimit() <= promotion.getUsageCount()) {
            log.warn("Promotion is not valid: {}", promotion.getId());
            throw new AppException(ErrorCode.PROMOTION_USAGE_LIMIT_EXCEEDED);
        }

        // Check if the promotion type is valid
        if (PromotionTypeEnum.PRODUCT_DISCOUNT.equals(promotion.getPromotionType())) {
            log.warn("Promotion is not valid: {}", promotion.getId());
            throw new AppException(ErrorCode.PROMOTION_IS_NOT_VALID);
        }

        // Check if the promotion meets the minimum order value requirement
        if (promotion.getMinOrderValue() != null && promotion.getMinOrderValue() > totalAmount) {
            log.warn("Promotion is not valid: {}", promotion.getId());
            throw new AppException(ErrorCode.ORDER_VALUE_NOT_ENOUGH);
        }

        // Check if the promotion is valid for the user
        if (promotion.getPromotionType() == PromotionTypeEnum.USER_PROMOTION) {
            List<Promotion> promotionsList = promotionRepository.findAvailableUserPromotions(userId, currentTime);
            Map<Long, Promotion> promotionMap = promotionsList.stream()
                    .collect(Collectors.toMap(Promotion::getId, p -> p));

            if (promotionMap.get(promotion.getId()) != null) {
                Promotion promotionUsed = promotionMap.get(promotion.getId());

                if(!promotionUsed.getUserPromotions().isEmpty()) {

                    Optional<UserPromotion> userPromotion = promotionRepository.findByUserIdAndPromotionId(userId, promotion.getId());

                    if (userPromotion.isPresent()) {
                        userPromotion.get().setUsed(true);

                    } else {
                        log.warn("Promotion is not valid: {}", promotion.getId());
                        throw new AppException(ErrorCode.PROMOTION_USED);
                    }
                }else {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                    promotionUsed.getUserPromotions().add(
                            UserPromotion.builder()
                                    .user(user)
                                    .promotion(promotionUsed)
                                    .used(true)
                                    .build());
                }
            }else {
                log.warn("Promotion is not valid: {}", promotion.getId());
                throw new AppException(ErrorCode.PROMOTION_USED);
            }
        }

        if (promotion.getUsageLimit() != null) {
            if (promotion.getUsageLimit() == promotion.getUsageCount() + 1) {
                log.warn("Promotion is not valid: {}", promotion.getId());
                throw new AppException(ErrorCode.PROMOTION_USAGE_LIMIT_EXCEEDED);
            }
        }
    }

    private int calculateDiscount(int totalAmount, Promotion promotion) {
        if (promotion.getDiscountUnit() == DiscountUnitEnum.PERCENT) {
            int temp = totalAmount * promotion.getDiscountValue() / 100;
            if (promotion.getMaxDiscountValue() != null) {
                return Math.min(temp, promotion.getMaxDiscountValue());
            } else {
                return temp;
            }
        } else {
            return promotion.getDiscountValue();
        }
    }

    private void handleUserPromotion(List<String> userIds, Promotion promotion) {
        if (promotion.getUserPromotions() == null) {
            promotion.setUserPromotions(new ArrayList<>());
        }

        List<UserPromotion> currentUserPromotions = promotion.getUserPromotions();

        Set<String> newUserIdSet = new HashSet<>(Optional.ofNullable(userIds).orElse(Collections.emptyList()));

        // 1. XÓA những userId không còn trong danh sách mới
        currentUserPromotions.removeIf(up ->
                up.getUser() != null && !newUserIdSet.contains(up.getUser().getId()));

        // 2. THÊM những userId mới chưa có
        Set<String> currentAfterRemove = currentUserPromotions.stream()
                .map(up -> up.getUser() != null ? up.getUser().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> toAdd = new HashSet<>(newUserIdSet);
        toAdd.removeAll(currentAfterRemove); // chỉ còn userId mới

        if (toAdd.isEmpty() && newUserIdSet.isEmpty()) {
            // TH1: Không có gì mới và đang là empty — giữ nguyên
            return;
        }

        if (newUserIdSet.isEmpty()) {
            // TH2: Rõ ràng là empty → dành cho tất cả user
            currentUserPromotions.clear();
            currentUserPromotions.add(UserPromotion.builder()
                    .promotion(promotion)
                    .build());
            return;
        }

        // TH3: Có userId mới → thêm vào
        List<User> users = userRepository.findAllById(toAdd);
        if (users.size() != toAdd.size()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        users.forEach(user -> {
            UserPromotion userPromotion = UserPromotion.builder()
                    .user(user)
                    .promotion(promotion)
                    .build();
            currentUserPromotions.add(userPromotion);
        });
    }

}
