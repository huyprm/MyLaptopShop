package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "serial_product_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerialProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialProductItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Builder.Default
    private boolean isActive = true;

    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "grn_detail_id")
    private GRNDetail grnDetail;
}
