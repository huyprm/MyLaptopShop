package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.Promotion;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Page<Promotion> findAll(Pageable pageable);

    Page<Promotion> findAllByPromotionType(PromotionTypeEnum promotionType, Pageable pageable);
}
