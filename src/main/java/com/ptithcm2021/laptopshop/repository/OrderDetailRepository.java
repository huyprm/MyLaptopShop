package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.OrderDetail;
import com.ptithcm2021.laptopshop.model.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

}
