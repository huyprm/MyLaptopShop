package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rank_levels")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private int minSpending;
    private int minOrder;
    private int priority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Builder.Default
    private boolean active = true;
}
