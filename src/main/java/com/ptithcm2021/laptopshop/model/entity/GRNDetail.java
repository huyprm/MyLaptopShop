package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grn_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GRNDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_id")
    private GoodsReceiptNote goodsReceiptNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_detail_id")
    private PurchaseOrderDetail purchaseOrderDetail;

    private Integer quantity;

    @OneToOne(cascade =  CascadeType.ALL, orphanRemoval = true, mappedBy = "grnDetail")
    private SerialProductItem serialNumber;
    private Integer unitPrice;
}
