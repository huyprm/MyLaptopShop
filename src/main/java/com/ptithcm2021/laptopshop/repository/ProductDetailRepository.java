package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductDetailProjection;
import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long>, JpaSpecificationExecutor<ProductDetail> {

    @Procedure(name = "sp_update_discount_price")
    void updateDiscountPrice();

    @Query(value = "SELECT pd.id, pd.original_price, pd.discount_price, pd.title, pd.thumbnail, colors.name, colors.hex " +
            "FROM product_details pd join colors on pd.color_id = colors.id " +
            "WHERE :keyword is null or to_tsvector('simple', pd.title) @@ plainto_tsquery('simple',:keyword)", nativeQuery = true)
    Page<ItemProductDetailProjection> searchProductDetails(String keyword,
                                                           Pageable pageable);
}
