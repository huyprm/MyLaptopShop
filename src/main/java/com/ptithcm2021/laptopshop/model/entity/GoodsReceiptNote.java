package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "goods_receipt_notes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class GoodsReceiptNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    private LocalDate receivedDate;
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_staff_id")
    private User staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @OneToMany(mappedBy = "goodsReceiptNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GRNDetail> grnDetail;

    private int totalQuantity;
}
