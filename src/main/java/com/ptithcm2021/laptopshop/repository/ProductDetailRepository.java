package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.dto.projection.DashboardStockLowProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductDetailProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long>, JpaSpecificationExecutor<ProductDetail> {

    @Procedure(name = "sp_update_discount_price")
    void updateDiscountPrice();

    @Query(value = "SELECT pd.id, pd.original_price, pd.discount_price, pd.title, pd.thumbnail, colors.name, colors.hex " +
            "FROM product_details pd join colors on pd.color_id = colors.id " +
            "WHERE :keyword is null or to_tsvector('simple', pd.title) @@ plainto_tsquery('simple',:keyword)", nativeQuery = true)
    Page<ItemProductDetailProjection> searchProductDetails(String keyword,
                                                           Pageable pageable);

    @Query("""
        SELECT
            pd.id as productDetailId,
            pd.title as title,
            pd.thumbnail as thumbnail,
            pd.originalPrice as originalPrice,
            pd.discountPrice as discountPrice,
            i.quantity as quantity
        FROM ProductDetail pd
        JOIN Inventory i ON pd.id = i.productDetail.id
        WHERE i.quantity < :threshold
        ORDER BY i.quantity ASC
        LIMIT :limit
""")
    List<DashboardStockLowProjection> getStockLowProducts(int limit, int threshold);


    @Query(value = """
                    SELECT pd.id
                    FROM product_details pd
                    WHERE (:keyword IS NULL OR to_tsvector('simple', pd.title) @@ plainto_tsquery('simple', :keyword))
            """, nativeQuery = true)
    List<Long> findAllProductDetailIdsByKeyword(String keyword);

    @Query("""
    SELECT new com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse(
        p.id as productId,
        pd.id as productDetailId,
        pd.originalPrice as originalPrice,
        pd.discountPrice as discountPrice,
        pd.thumbnail as thumbnail,
        pd.totalRating as totalRating,
        pd.soldQuantity as soldQuantity,
        pd.title as title,
        pd.warrantyProd as warrantyProd,
        img as itemImage,
        pd.inventory.quantity as quantity,
        pd.createdDate as createdDate,
        pd.promotionIdMaxDiscount as promotionIdMaxDiscount
    )
    FROM Product p
    JOIN ProductDetail pd ON p.id = pd.product.id
    JOIN pd.images img
    WHERE img = (
             SELECT MIN(i)
             FROM ProductDetail pd2
             JOIN pd2.images i
             WHERE pd2.id = pd.id
         )
""")
    Page<ItemProductResponse> findProducts(Specification<ProductDetail> spec, Pageable pageable);
}
