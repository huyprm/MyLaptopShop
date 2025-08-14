package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.event.CreateUserPromotionEvent;
import com.ptithcm2021.laptopshop.event.EventPublisherHelper;
import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.PromotionMapper;
import com.ptithcm2021.laptopshop.model.dto.projection.VoucherProjection;
import com.ptithcm2021.laptopshop.model.dto.request.PromotionRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Promotion.PromotionResponse;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.PromotionService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
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
    private final RankLevelRepository rankLevelRepository;
    private final UserPromotionRepository userPromotionRepository;
    private final EntityManager entityManager;
    private final EventPublisherHelper eventPublisherHelper;

    @Override
    @Transactional
    public PromotionResponse addPromotion(PromotionRequest request) {
        if (request.getEndDate() != null &&
                request.getEndDate().isBefore(request.getStartDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        String userId = FetchUserIdUtil.fetchUserId();
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User userCreate = userRepository.getReferenceById(userId);

        Promotion promotion = promotionMapper.toPromotion(request);

        promotion.setUserCreate(userCreate);
        promotionRepository.save(promotion);

        if (request.getPromotionType() == PromotionTypeEnum.PRODUCT_DISCOUNT) {
            if (request.getProductDetailIds() == null || request.getProductDetailIds().isEmpty()) {
                throw new AppException(ErrorCode.LIST_PRODUCT_EMPTY);
            }
            handleProductPromotions(request.getProductDetailIds(), promotion);

        }else if (request.getPromotionType() == PromotionTypeEnum.USER_PROMOTION){

            if( request.getUserIds() == null || request.getUserIds().isEmpty()) {
                throw new AppException(ErrorCode.LIST_USER_EMPTY);
            }
            handleUserPromotion(request.getUserIds(), promotion);

        }else if (request.getPromotionType() == PromotionTypeEnum.GIFT){

            if( request.getProductDetailIds() != null && !request.getProductDetailIds().isEmpty()) {
                handleGiftPromotion(request.getRankLevelIds(), promotion);
            }

        }else if (request.getPromotionType() == PromotionTypeEnum.SHOP_DISCOUNT){
            handleShopPromotion(request);
        }
        return promotionMapper.toPromotionResponse(promotion);
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(PromotionRequest request, long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        promotionMapper.updatePromotion(request, promotion);

        validatePromotionDate(LocalDateTime.now(), promotion);

        if (request.getPromotionType() == PromotionTypeEnum.PRODUCT_DISCOUNT) {
            if (request.getProductDetailIds() == null || request.getProductDetailIds().isEmpty()) {
                throw new AppException(ErrorCode.LIST_PRODUCT_EMPTY);
            }
            handleProductPromotions(request.getProductDetailIds(), promotion);

        }else if (request.getPromotionType() == PromotionTypeEnum.USER_PROMOTION){

            if( request.getUserIds() == null || request.getUserIds().isEmpty()) {
                throw new AppException(ErrorCode.LIST_USER_EMPTY);
            }
            handleUserPromotion(request.getUserIds(), promotion);

        }else if (request.getPromotionType() == PromotionTypeEnum.GIFT){

            if( request.getProductDetailIds() != null && !request.getProductDetailIds().isEmpty()) {
                handleGiftPromotion(request.getRankLevelIds(), promotion);
            }
        }

        return promotionMapper.toPromotionResponse(promotionRepository.save(promotion));
    }

    @Override
    public void deletePromotion(long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

//        validatePromotionDate(LocalDateTime.now(), promotion);

        try {
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
    public PageWrapper<PromotionResponse> getPromotions(String keyword, PromotionStatusEnum status, PromotionTypeEnum promotionType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isBlank()) {
            keyword = "%" + keyword.toLowerCase() + "%";
        }

        String statusName = null;
        if (status !=null) {
            statusName = status.name();
        }
        Page<Promotion> promotion = promotionRepository.findAllPromotionsByFilter(keyword, statusName, promotionType, pageable);
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

        if (statusEnum == PromotionStatusEnum.INACTIVE)
            return promotionRepository.findUpcomingPromotionsByProductDetailId(id, LocalDateTime.now())
                    .stream()
                    .map(promotionMapper::toPromotionResponse)
                    .toList();

        return null;
    }

    @Override
    public PromotionResponse getShopPromotionsIsActive() {
        return promotionMapper.toPromotionResponse(promotionRepository.findShopPromotion());
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
        try {
            lock.lock(5, TimeUnit.SECONDS);


            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));


            if (promotion.getPromotionType() == PromotionTypeEnum.PRODUCT_DISCOUNT) {
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

    @Override
    public PageWrapper<PromotionDetailResponse> getPromotionDetailsByType(long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        switch (promotion.getPromotionType()) {
            case USER_PROMOTION: {
                Page<UserPromotion> userPromotionPage = promotionRepository.findUserPromotionsByPromotionId(id, pageable);

                return PageWrapper.<PromotionDetailResponse>builder()
                        .content(userPromotionPage.stream().map(promotionMapper::toPromotionDetailResponse).toList())
                        .pageNumber(userPromotionPage.getNumber())
                        .pageSize(userPromotionPage.getSize())
                        .totalElements(userPromotionPage.getTotalElements())
                        .totalPages(userPromotionPage.getTotalPages())
                        .build();
            }
            case GIFT: {
                Page<UserPromotion> userPromotionPage = promotionRepository.findUserPromotionsByPromotionId(id, pageable);
                List<PromotionDetailResponse> userPromotion;
                if(userPromotionPage.isEmpty()){
                    userPromotion = List.of(promotionMapper.toPromotionDetailResponse("This promotion applies to users according to rank"));
                }else {
                    userPromotion = userPromotionPage.stream()
                            .map(promotionMapper::toPromotionDetailResponse)
                            .toList();
                }
                return PageWrapper.<PromotionDetailResponse>builder()
                        .content(userPromotion)
                        .pageNumber(userPromotionPage.getNumber())
                        .pageSize(userPromotionPage.getSize())
                        .totalElements(userPromotionPage.getTotalElements())
                        .totalPages(userPromotionPage.getTotalPages())
                        .build();
            }
            case PRODUCT_DISCOUNT:
               Page<ProductPromotion> productPromotionPage = promotionRepository.findProductPromotionsByPromotionId(id, pageable);

               return PageWrapper.<PromotionDetailResponse>builder()
                        .content(productPromotionPage.stream().map(promotionMapper::toPromotionDetailResponse).toList())
                        .pageNumber(productPromotionPage.getNumber())
                        .pageSize(productPromotionPage.getSize())
                        .totalElements(productPromotionPage.getTotalElements())
                        .totalPages(productPromotionPage.getTotalPages())
                        .build();
            case SHOP_DISCOUNT:
                return PageWrapper.<PromotionDetailResponse>builder()
                        .content(List.of(promotionMapper.toPromotionDetailResponse("This promotion is for the entire shop")))
                        .pageNumber(0)
                        .pageSize(0)
                        .totalElements(0)
                        .totalPages(0)
                        .build();
            case ALL_USER:
                return PageWrapper.<PromotionDetailResponse>builder()
                        .content(List.of(promotionMapper.toPromotionDetailResponse("This promotion applies to all users")))
                        .pageNumber(0)
                        .pageSize(0)
                        .totalElements(0)
                        .totalPages(0)
                        .build();
            case ALL_PRODUCT:
                return PageWrapper.<PromotionDetailResponse>builder()
                        .content(List.of(promotionMapper.toPromotionDetailResponse("This promotion applies to all products")))
                        .pageNumber(0)
                        .pageSize(0)
                        .totalElements(0)
                        .totalPages(0)
                        .build();
        }
        throw new AppException(ErrorCode.PROMOTION_NOT_FOUND);
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
            if (promotion.getUsageLimit() < promotion.getUsageCount() + 1) {
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
        PromotionTypeEnum type = promotion.getPromotionType();

        if (type == PromotionTypeEnum.USER_PROMOTION ||
                type == PromotionTypeEnum.GIFT ||
                type == PromotionTypeEnum.ALL_USER) {

            List<Promotion> promotionsList = promotionRepository.findAvailableUserPromotions(userId, currentTime);

            Map<Long, Promotion> promotionMap = promotionsList.stream()
                    .collect(Collectors.toMap(Promotion::getId, p -> p));

            if (promotionMap.get(promotion.getId()) != null) {
                Promotion promotionUsed = promotionMap.get(promotion.getId());

                if (!promotionUsed.getUserPromotions().isEmpty()) {

                    Optional<UserPromotion> userPromotion = promotionRepository.findByUserIdAndPromotionId(userId, promotion.getId());

                    if (userPromotion.isPresent()) {
                        userPromotion.get().setUsed(true);

                    } else {
                        log.warn("Promotion is not valid: {}", promotion.getId());
                        throw new AppException(ErrorCode.PROMOTION_USED);
                    }
                } else {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                    promotionUsed.getUserPromotions().add(
                            UserPromotion.builder()
                                    .user(user)
                                    .promotion(promotionUsed)
                                    .used(true)
                                    .build());
                }
            } else {
                log.warn("Promotion is not valid: {}", promotion.getId());
                throw new AppException(ErrorCode.PROMOTION_USED);
            }
        }

        if (promotion.getUsageLimit() != null) {
            if (promotion.getUsageLimit() < promotion.getUsageCount() + 1) {
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

        List<UserPromotion> currentUserPromotions = Optional.ofNullable(promotion.getUserPromotions())
                .orElse(Collections.emptyList());


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

            return;
        }

        // TH3: Có userId mới → thêm vào
        long found = userRepository.countByIdIn(toAdd);
        if (found != toAdd.size()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        eventPublisherHelper.publish(new CreateUserPromotionEvent(toAdd, promotion));

    }

    private void handleGiftPromotion(List<Integer> rankLevelIds, Promotion promotion) {
        List<Integer> newRankLevelIdSet = rankLevelIds.stream().distinct().toList();
        long countRankLevelIsValid = rankLevelRepository.countByActiveRankIds(newRankLevelIdSet);

        if (rankLevelIds.size() != countRankLevelIsValid) {
            throw new AppException(ErrorCode.RANK_INVALID);
        }

        List<String> userIds = userRepository.findUserIdsByRankIds(newRankLevelIdSet);

        handleUserPromotion(userIds, promotion);
    }

    @Transactional
    public void handleEventCreateUserPromotion(Set<String> userIds, Promotion promotion) {
        int count = 0;
        int BATCH_SIZE = 1000;

        for (String userId : userIds) {
            User userRef = entityManager.getReference(User.class, userId);

            UserPromotion up = new UserPromotion();
            up.setUser(userRef);
            up.setPromotion(promotion);
            up.setCollectDate(LocalDateTime.now());
            up.setUsed(false);
            up.setUsedDate(null);

            entityManager.persist(up);
            count++;

            if (count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
                log.info("Flushed batch of {}", count);

            }
        }

        // Flush phần còn lại
        entityManager.flush();
        entityManager.clear();

// version chưa tối ưu
//        List<UserPromotion> newUserPromotions = toAdd.stream()
//                .map(userId -> {
//                    User userRef = entityManager.getReference(User.class, userId);
//                    return UserPromotion.builder()
//                            .user(userRef)
//                            .promotion(promotion)
//                            .collectDate(LocalDateTime.now())
//                            .used(false)
//                            .usedDate(null)
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//        userPromotionRepository.saveAll(newUserPromotions);

    }

    private void handleShopPromotion(PromotionRequest request) {
        Promotion promotionRef = promotionRepository.findShopPromotion();

        if (promotionRef != null) {
            if (promotionRef.getEndDate() != null && promotionRef.getEndDate().isAfter(request.getStartDate())) {
                throw new AppException(ErrorCode.SHOP_PROMOTION_ACTIVE);
            }
            if (promotionRef.getEndDate() == null){
                throw new AppException(ErrorCode.SHOP_PROMOTION_ACTIVE);
            }
        }
    }
}