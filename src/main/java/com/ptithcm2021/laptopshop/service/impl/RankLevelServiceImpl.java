package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.RankLevelMapper;
import com.ptithcm2021.laptopshop.model.dto.request.RankLevelRequest;
import com.ptithcm2021.laptopshop.model.dto.response.RankLevelResponse;
import com.ptithcm2021.laptopshop.model.dto.response.RankUserResponse;
import com.ptithcm2021.laptopshop.model.entity.Promotion;
import com.ptithcm2021.laptopshop.model.entity.RankLevel;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import com.ptithcm2021.laptopshop.repository.PromotionRepository;
import com.ptithcm2021.laptopshop.repository.RankLevelRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.RankLevelService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
/**
 * Cannot update rank, only allow,
 * if rank is wrong then delete or disable activity and create new one
 */
@Service
@RequiredArgsConstructor
public class RankLevelServiceImpl implements RankLevelService {
    private final RankLevelRepository rankLevelRepository;
    private final RankLevelMapper rankLevelMapper;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public RankLevelResponse createRankLevel(RankLevelRequest request) {
        RankLevel rankLevel = rankLevelMapper.toRankLevel(request);

        if (request.getPromotionId() != null) {
            Promotion promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
            if (!promotion.getPromotionType().equals(PromotionTypeEnum.GIFT)){
                throw new AppException(ErrorCode.PROMOTION_NOT_GIFT);
            }
            rankLevel.setPromotion(promotion);
        }

        rankLevelRepository.save(rankLevel);

        List<RankLevel> existingRankLevels = rankLevelRepository.findAllByIsActive();
        if (!existingRankLevels.isEmpty()) {
            validateRankLevel(existingRankLevels);
        }
        return rankLevelMapper.toResponse(rankLevel);
    }

    @Override
    @Transactional
    public RankLevelResponse updateRankLevel(int id, RankLevelRequest request) {
        RankLevel rankLevel = rankLevelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RANK_NOT_FOUND));

        if (request.getPromotionId() != null && !request.getPromotionId().equals(rankLevel.getPromotion().getId())) {
            Promotion promotion = promotionRepository.findById(request.getPromotionId())
                    .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
            if (!promotion.getPromotionType().equals(PromotionTypeEnum.GIFT)){
                throw new AppException(ErrorCode.PROMOTION_NOT_GIFT);
            }
            rankLevel.setPromotion(promotion);
        }
        rankLevelRepository.save(rankLevel);

        List<RankLevel> existingRankLevels = rankLevelRepository.findAllByIsActive();
        if (!existingRankLevels.isEmpty()) {
            validateRankLevel(existingRankLevels);
        }
        return rankLevelMapper.toResponse(rankLevel);
    }

    @Override
    public void disableRankLevel(int id) {
        RankLevel rankLevel = rankLevelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RANK_NOT_FOUND));
        rankLevel.setActive(false);
        rankLevelRepository.save(rankLevel);
    }

    @Override
    public RankLevelResponse getRankLevelById(int id) {
        RankLevel rankLevel = rankLevelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RANK_NOT_FOUND));
        return rankLevelMapper.toResponse(rankLevel);
    }

    @Override
    public void deleteRankLevel(int id) {
        if (!rankLevelRepository.existsById(id)) {
            throw new AppException(ErrorCode.RANK_NOT_FOUND);
        }
        try {
            rankLevelRepository.deleteById(id);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }

    }

    @Override
    public List<RankLevelResponse> getAllRankLevelsIsActive() {
        List<RankLevel> rankLevels = rankLevelRepository.findAllByIsActive();
        return rankLevels.stream()
                .sorted(Comparator.comparing(RankLevel::getPriority))
                .map(rankLevelMapper::toResponse)
                .toList();
    }

    @Override
    public RankUserResponse getRankLevelByUser() {
        String userId = FetchUserIdUtil.fetchUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        RankLevel currentRankLevel = user.getCurrentRankLevel();
        RankLevel nextRankLevel = rankLevelRepository
                .findNextRankIsActive(currentRankLevel.getPriority())
                .orElse(null);

        return RankUserResponse.builder()
                .currentRank(rankLevelMapper.toResponse(currentRankLevel))
                .nextRank(nextRankLevel != null ? rankLevelMapper.toResponse(nextRankLevel) : null)
                .amountUsed(user.getAmountUsed())
                .amountOrder(user.getAmountOrder())
                .spendingToNextRank(nextRankLevel != null ? nextRankLevel.getMinSpending() - user.getAmountUsed() : 0)
                .ordersToNextRank(nextRankLevel != null ? nextRankLevel.getMinOrder() - user.getAmountOrder() : 0)
                .build();
    }

    private void validateRankLevel(List<RankLevel> rankLevels) {
        rankLevels.sort(Comparator.comparing(RankLevel::getPriority));

        int prevUsed = Integer.MIN_VALUE;
        int prevOrder = Integer.MIN_VALUE;
        for (RankLevel rankLevel : rankLevels) {
            if (rankLevel.getMinSpending() < prevUsed) {
                throw new AppException(ErrorCode.RANK_LEVEL_MIN_SPENDING_INVALID);

            }
            prevUsed = rankLevel.getMinSpending();
        }
    }


}
