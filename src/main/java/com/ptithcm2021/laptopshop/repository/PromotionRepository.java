package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.Promotion;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequestMapping
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Page<Promotion> findAll(Pageable pageable);

    Page<Promotion> findAllByPromotionType(PromotionTypeEnum promotionType, Pageable pageable);

    @Query("select p.id from ProductPromotion pp join Promotion p on pp.promotion.id = p.id " +
            "where pp.productDetail.id = :productDetailId and pp.promotion.startDate <= :currentDate and pp.promotion.endDate >= :currentDate")
    List<Long> findProductPromotionIdsByPIdAndDate(Long productDetailId,
                                                           LocalDateTime currentDate);

    @Query("select p.id from UserPromotion up join Promotion p on up.promotion.id = p.id " +
            "where up.user.id = :userId and p.startDate <= :currentDate and p.endDate >= :currentDate")
    List<Long> findUserPromotionIdsByUserIdAndDate(String userId,
                                                 LocalDateTime currentDate);

    @Query("select p from ProductPromotion pp join Promotion p on pp.promotion.id = p.id " +
            "where pp.productDetail.id = :productDetailId and pp.promotion.startDate <= :currentDate and pp.promotion.endDate >= :currentDate")
    List<Promotion> findProductPromotionsIsActive(Long productDetailId,
                                                 LocalDateTime currentDate);

    @Query("select p from ProductPromotion pp join Promotion p on pp.promotion.id = p.id " +
            "where pp.productDetail.id = :productDetailId and pp.promotion.endDate < :currentDate")
    List<Promotion> findProductPromotionsIsExpired(long productDetailId,
                                                   LocalDateTime currentDate);

    @Query("select p from ProductPromotion pp join Promotion p on pp.promotion.id = p.id " +
            "where pp.productDetail.id = :productDetailId and pp.promotion.startDate > :currentDate")
    List<Promotion> findProductPromotionIdsInActive(long productDetailId,
                                                    LocalDateTime currentDate);

    @Query("select p from ProductPromotion pp join Promotion p on pp.promotion.id = p.id " +
            "where pp.productDetail.id = :productDetailId")
    List<Promotion> findProductPromotions(long productDetailId);
}
