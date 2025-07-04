//package com.ptithcm2021.laptopshop.model.entity;
//
//import com.ptithcm2021.laptopshop.model.enums.PromotionTypeEnum;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "coupon_codes")
//@Setter
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class CouponCode {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "promotion_id")
//    private Promotion promotion;
//
//    private String code;
//    private Integer usageLimit;
//    private Integer usageCount;
//    private Integer perUserLimit;
//    private Boolean active;
//}
