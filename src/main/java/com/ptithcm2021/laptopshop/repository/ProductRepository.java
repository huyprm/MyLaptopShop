package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

        @Query(value = """
    SELECT
        p.id AS productId,
        d.original_price AS originalPrice,
        d.discount_price AS discountPrice,
        d.thumbnail AS thumbnail,
        d.total_rating AS totalRating,
        d.sold_quantity AS soldQuantity,
        d.title as title
    FROM
        products p
    JOIN brands b ON p.brand_id = b.id
    JOIN series s ON p.series_id = s.id
    JOIN categories c ON p.category_id = c.id
    JOIN product_details d ON d.product_id = p.id
    WHERE
        (
            setweight(to_tsvector('simple', b.name), 'A') ||
            setweight(to_tsvector('simple', s.name), 'A') ||
            setweight(to_tsvector('simple', c.name), 'B') ||
            setweight(to_tsvector('simple', d.title), 'C')
        ) @@ plainto_tsquery('simple', :keyword)
    """,
                countQuery = """
    SELECT COUNT(*)
    FROM
        products p
    JOIN brands b ON p.brand_id = b.id
    JOIN series s ON p.series_id = s.id
    JOIN categories c ON p.category_id = c.id
    JOIN product_details d ON d.product_id = p.id
    WHERE
        (
            setweight(to_tsvector('simple', b.name), 'A') ||
            setweight(to_tsvector('simple', s.name), 'A') ||
            setweight(to_tsvector('simple', c.name), 'B') ||
            setweight(to_tsvector('simple', d.title), 'C')
        ) @@ plainto_tsquery('simple', :keyword)
    """,
                nativeQuery = true)
        Page<ItemProductProjection> searchFullText(
                @Param("keyword") String keyword,
                Pageable pageable);

    @Query("""
        SELECT
            p.id AS productId,
            d.originalPrice AS originalPrice,
            d.discountPrice AS discountPrice,
            d.thumbnail AS thumbnail,
            d.totalRating AS totalRating,
            d.soldQuantity AS soldQuantity,
            d.title as title
        FROM Product p
        JOIN p.productDetails d
        WHERE d.discountPrice =
            (SELECT MIN(d2.discountPrice)
                FROM ProductDetail d2
                    WHERE d2.product = p)
    """)
    Page<ItemProductProjection> findOneProductDetailOfProductProjection(Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}
