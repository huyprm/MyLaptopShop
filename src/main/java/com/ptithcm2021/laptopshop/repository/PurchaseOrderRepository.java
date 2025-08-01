package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.PurchaseOrder;
import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    int countByCodeStartingWith(String baseCode);

    Optional<PurchaseOrder> findByCode(String purchaseOrderCode);

    @Query(value = """
        select po from PurchaseOrder po
        where (:statusEnum is null or po.status = :statusEnum)
        and (:keyword is null or po.code like :keyword)
            """)
    Page<PurchaseOrder>findByStatusAndCodeContaining(
            @Param("statusEnum") PurchaseOrderStatusEnum statusEnum,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
