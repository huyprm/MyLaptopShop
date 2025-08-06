package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.dto.projection.VoucherProjection;
import com.ptithcm2021.laptopshop.model.entity.ProductPromotion;
import com.ptithcm2021.laptopshop.model.entity.Promotion;
import com.ptithcm2021.laptopshop.model.entity.UserPromotion;
import com.ptithcm2021.laptopshop.model.enums.PromotionStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Page<Promotion> findAllByPromotionType(PromotionTypeEnum promotionType, Pageable pageable);

    @Query("select p.id from Promotion p " +
            "LEFT join ProductPromotion pp ON pp.promotion.id = p.id " +
            "where (p.promotionType = 'PRODUCT_DISCOUNT' or p.promotionType = 'ALL_PRODUCT') " +
            "and p.startDate <= :currentDate " +
            "and (p.endDate is null or p.endDate >= :currentDate) " +
            "and (pp.productDetail.id = :productDetailId or pp.productDetail is null)")
    List<Long> findAvailableProductPromotions(@Param("productDetailId") Long productDetailId,
                                              @Param("currentDate") LocalDateTime currentDate);


    @Query("select p from Promotion p " +
            "left join UserPromotion up on up.promotion.id = p.id " +
            "where (p.promotionType = 'USER_PROMOTION' or p.promotionType = 'ALL_USER')" +
            "and p.startDate <= :currentDate " +
            "and (p.endDate is null or p.endDate >= :currentDate) " +
            "and (up.user.id = :userId or up.user is null) " +
            "and (up.used is null or up.used = false)")
    List<Promotion> findAvailableUserPromotions(String userId, LocalDateTime currentDate);



    @Query("""
    select p from Promotion p
    left join ProductPromotion pp on pp.promotion.id = p.id
    where (p.promotionType = 'PRODUCT_DISCOUNT' or p.promotionType = 'ALL_PRODUCT')
      and p.startDate <= :currentDate
      and (p.endDate is null or p.endDate >= :currentDate)
      and (pp.productDetail.id = :productDetailId or pp.productDetail is null)
""")
    List<Promotion> findProductPromotionsIsActive(Long productDetailId, LocalDateTime currentDate);


    @Query("""
    select p from Promotion p
    left join ProductPromotion pp on pp.promotion.id = p.id
    where (p.promotionType = 'PRODUCT_DISCOUNT' or p.promotionType = 'ALL_PRODUCT')
      and p.endDate is not null
      and p.endDate < :currentDate
      and (pp.productDetail.id = :productDetailId or pp.productDetail is null)
""")
    List<Promotion> findProductPromotionsIsExpired(Long productDetailId, LocalDateTime currentDate);

    @Query("""
    select p from Promotion p
    left join ProductPromotion pp on pp.promotion.id = p.id
    where (p.promotionType = 'PRODUCT_DISCOUNT' or p.promotionType = 'ALL_PRODUCT')
      and p.startDate > :currentDate
      and (pp.productDetail.id = :productDetailId or pp.productDetail is null)
""")
    List<Promotion> findUpcomingPromotionsByProductDetailId(Long productDetailId, LocalDateTime currentDate);

    @Query("select p from ProductPromotion pp join Promotion p on pp.promotion.id = p.id " +
            "where pp.productDetail.id = :productDetailId")
    List<Promotion> findProductPromotions(long productDetailId);


    @Modifying
    @Query(value = "CALL sp_delete_user_promotion_expired_used();", nativeQuery = true)
    void deleteUserPromotionExpiredOrUsed();

    @Query("select up from UserPromotion up " +
            "where up.user.id = :userId and up.promotion.id = :promotionId and up.used = false")
    Optional<UserPromotion> findByUserIdAndPromotionId(String userId, Long promotionId);

    @Query("""
    select
           p.id as id,
           p.name as name,
           p.code as code,
           p.description as description,
           p.promotionType as promotionType,
           p.discountValue as discountValue,
           p.discountUnit as discountUnit,
           p.maxDiscountValue as maxDiscountValue,
           p.minOrderValue as minOrderValue,
           p.usageLimit as usageLimit,
           p.usageCount as usageCount,
           p.startDate as startDate,
           p.endDate as endDate,
           coalesce(up.used, false) as used
    from Promotion p
    left join UserPromotion up on up.promotion.id = p.id
    where (p.promotionType = 'USER_PROMOTION' or p.promotionType = 'ALL_USER')
    and (p.endDate is null or p.endDate >= current_timestamp)
    and (up.user is null or up.user.id = :userId)
    and (up.used is null or up.used = false)
""")
    List<VoucherProjection> findUserVouchers(@Param("userId") String userId);


    @Query("SELECT up FROM UserPromotion up JOIN FETCH up.user WHERE up.promotion.id = :id")
    Page<UserPromotion> findUserPromotionsByPromotionId(@Param("id") long id, Pageable pageable);

    @Query("SELECT pp FROM ProductPromotion pp JOIN FETCH pp.productDetail WHERE pp.promotion.id = :id")
    Page<ProductPromotion> findProductPromotionsByPromotionId(long id, Pageable pageable);

    @Query("""
   SELECT p FROM Promotion p
   WHERE (:keyword IS NULL OR p.code LIKE CONCAT('%', :keyword, '%'))
     AND (:type IS NULL OR p.promotionType = :type)
     AND (
           :status IS NULL
           OR (:status = 'ACTIVE' AND p.startDate <= CURRENT_TIMESTAMP AND (p.endDate IS NULL OR p.endDate >= CURRENT_TIMESTAMP))
           OR (:status = 'EXPIRED' AND p.endDate < CURRENT_TIMESTAMP)
           OR (:status = 'INACTIVE' AND p.startDate > CURRENT_TIMESTAMP)
       )
""")
    Page<Promotion> findAllPromotionsByFilter(@Param("keyword") String keyword,
                                              @Param("status") String status,
                                              @Param("type") PromotionTypeEnum type,
                                              Pageable pageable);
}
