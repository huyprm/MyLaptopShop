package com.ptithcm2021.laptopshop.model.entity;

import com.ptithcm2021.laptopshop.model.enums.DiscountUnitEnum;
import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private PromotionTypeEnum promotionType;

    private String discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountUnitEnum discountUnit;

    private Integer minOrderValue;
    private Integer maxDiscountValue;
    private Integer usageLimit;
    private Integer usageCount;

    @Builder.Default
    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_create_id",  nullable = false)
    private User userCreate;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "promotion",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProductPromotion> productPromotions;

    @OneToMany(mappedBy = "promotion")
    private List<UserPromotion>  userPromotions;

    @OneToOne(mappedBy = "promotion")
    private RankLevel rankLevel;
}
