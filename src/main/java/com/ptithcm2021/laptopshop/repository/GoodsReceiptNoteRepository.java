package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.GoodsReceiptNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote, Long> {
    int countByCodeStartingWith(String baseCode);

    @Query("SELECT COALESCE(sum (g.quantity), 0) FROM GRNDetail g WHERE g.purchaseOrderDetail.id = :purchaseOrderDetailId")
    int sumReceivedByPurchaseOrderDetailId(Long purchaseOrderDetailId);

    @Query("SELECT COALESCE(sum (g.totalQuantity), 0) FROM GoodsReceiptNote g WHERE g.purchaseOrder.id = :purchaseOrderId")
    int sumReceivedByPurchaseOrderId(Long purchaseOrderId);

    Optional<GoodsReceiptNote> findByCode(String code);

    @Query("SELECT g FROM GoodsReceiptNote g WHERE :grnCode is null or g.code like :grnCode")
    Page<GoodsReceiptNote> findAllByGRNCode(String grnCode,
                                                    Pageable pageable);

    @Query("SELECT COUNT(g) FROM GRNDetail g WHERE g.productDetail.id = :productDetailId")
    int countGRNByProductDetailId(Long productDetailId);

    Page<GoodsReceiptNote> findAllByPurchaseOrderId(long purchaseOrderId,
                                                    Pageable pageable);

    @Query("SELECT coalesce(sum(g.totalPrice),0) FROM GoodsReceiptNote g WHERE g.receivedDate BETWEEN :from AND :to")
    long sumTotalCost(LocalDate from, LocalDate to);
}
