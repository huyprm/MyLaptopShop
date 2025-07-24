package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailId implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private Long productDetailId;
}
