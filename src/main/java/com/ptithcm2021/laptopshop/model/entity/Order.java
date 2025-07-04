package com.ptithcm2021.laptopshop.model.entity;

import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pdfUrl;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @CreatedDate
    private LocalDateTime createdDate;

    private Integer amount;

    @OneToOne(mappedBy = "order",fetch = FetchType.LAZY)
    private UserPromotion userPromotion;
}
