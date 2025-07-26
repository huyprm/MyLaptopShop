package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.PurchaseOrder;
import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    int countByCodeStartingWith(String baseCode);

    Page<PurchaseOrder> findAllByStatus(PurchaseOrderStatusEnum statusEnum, Pageable pageable);

    Page<PurchaseOrder> findAll(Pageable pageable);

    Optional<PurchaseOrder> findByCode(String purchaseOrderCode);


}
