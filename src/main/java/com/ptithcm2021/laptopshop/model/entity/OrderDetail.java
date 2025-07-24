package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "order_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {
    @EmbeddedId
    private OrderDetailId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productDetailId")
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    // Để trả hoàn lại lượt sử dụng mã khuyến mãi khi cần
    private Long productPromotionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer quantity;

    private Integer originalPrice; // Original price before discount
    private Integer price; // Price after discount

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> serialNumber; 
}
