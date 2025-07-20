package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventories")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
    @Id
    private Long productDetailId;

    @MapsId("productDetailId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    private Integer quantity;
}
