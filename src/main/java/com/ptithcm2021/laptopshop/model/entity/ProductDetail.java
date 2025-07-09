package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(mappedBy = "productDetail", fetch = FetchType.LAZY)
    private Config config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;

    private int originalPrice;
    private int discountPrice;
    private String slug;
    private String thumbnail;
    private String title;

    @Builder.Default
    private double totalRating = 0.0;
    @Builder.Default
    private int soldQuantity = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;

    @CreatedDate
    private LocalDateTime createdDate;
}
