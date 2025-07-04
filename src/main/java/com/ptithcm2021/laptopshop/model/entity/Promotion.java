package com.ptithcm2021.laptopshop.model.entity;

import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

//    @Enumerated(EnumType.STRING)
//    private PromotionTypeEnum promotionType;

    private String discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountUnitEnum discountUnit;

    private Integer minOrderValue;
    private Integer maxDiscountValue;
    private Integer usageLimit;
    private Integer usageCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_create_id")
    private User userCreate;

    @CreatedDate
    private LocalDateTime createdAt;

}
