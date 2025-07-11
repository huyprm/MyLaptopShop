package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.Cart;
import com.ptithcm2021.laptopshop.model.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
    List<Cart> findAllByUserId(String userId);
}
