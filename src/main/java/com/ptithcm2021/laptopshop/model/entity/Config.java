package com.ptithcm2021.laptopshop.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpu;
    private String madeIn;
    private String displaySize;
    private String graphicCard;
    private String ram;
    private String ramValue;
    private String weight;
    private String hardDrive;
    private String hardDriveValue;
    private String nameConfig;

    @OneToOne()
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;
}

