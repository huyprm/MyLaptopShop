package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_promotions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_promotion_seq")
    @SequenceGenerator(name = "user_promotion_seq", sequenceName = "user_promotions_id_seq", allocationSize = 100)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @CreatedDate
    private LocalDateTime collectDate;

    @Builder.Default
    private Boolean used = false;

    private LocalDateTime usedDate;
}
