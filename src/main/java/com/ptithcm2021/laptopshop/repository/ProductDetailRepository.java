package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.springframework.data.domain.Page;
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
}
