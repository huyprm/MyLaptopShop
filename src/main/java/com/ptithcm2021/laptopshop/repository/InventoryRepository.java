package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT i.quantity FROM Inventory i WHERE i.productDetail.id = :id")
    Optional<Integer> findQuantityById(@Param("id") Long id);

    @Query("select count(*) from InventoryTransaction where productDetail.id = :id and transactionType = 'IMPORT'")
    Integer countImportTransactionsByProductDetailId(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productDetail.id = :id")
    Optional<Inventory> findInventoryForUpdate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity + :amount WHERE i.productDetail.id = :id")
    void increaseQuantity(@Param("id") Long id, @Param("amount") int amount);

}
