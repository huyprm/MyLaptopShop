package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.SerialProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerialProductItemRepository extends JpaRepository<SerialProductItem, Long> {
    @Modifying
    @Query("UPDATE SerialProductItem s SET s.isActive = false " +
            "WHERE s.serialNumber = :serialNumber " +
            "AND s.productDetail.id = :productDetailId " +
            "AND s.isActive = true")
    int deactivateIfExists(@Param("serialNumber") String serialNumber,
                           @Param("productDetailId") Long productDetailId);

    boolean existsBySerialNumber(String serial);

    Optional<SerialProductItem> findBySerialNumber(String serialNumber);

    @Query("SELECT s.serialNumber FROM SerialProductItem s " +
           "WHERE s.productDetail.id = :productDetailId AND s.isActive = true")
    List<String> findAllSerialNumbersByProductDetailIdAndActive(Long productDetailId);
}
