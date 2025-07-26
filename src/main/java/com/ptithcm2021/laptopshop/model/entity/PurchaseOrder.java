package com.ptithcm2021.laptopshop.model.entity;

import com.ptithcm2021.laptopshop.model.enums.PurchaseOrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.Join;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @CreatedDate
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_order_id")
    private User userOrder;

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatusEnum status;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseOrderDetail> details;

    @Column(columnDefinition = "TEXT")
    private String note;

    private int totalQuantity;
}
