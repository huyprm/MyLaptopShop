package com.ptithcm2021.laptopshop.model.entity;

import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String pdfUrl;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime completedAt;

    private int totalQuantity;
    private int totalPrice;
    private int shopDiscount;
    private int userDiscount;

    private String address;
    private String phone;
    private String recipient;
    private String note;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
