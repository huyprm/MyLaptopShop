package com.ptithcm2021.laptopshop.model.dto.response.Product;

import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigResponse {
    private long id;
    private String cpu;
    private String madeIn;
    private String displaySize;
    private String graphicCard;
    private String ram;
    private String weight;
    private String hardDriver;
    private String nameConfig;
    private long productDetailId;
}
