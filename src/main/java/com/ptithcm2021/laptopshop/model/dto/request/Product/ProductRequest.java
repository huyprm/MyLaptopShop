package com.ptithcm2021.laptopshop.model.dto.request.Product;

import com.ptithcm2021.laptopshop.model.entity.Brand;
import com.ptithcm2021.laptopshop.model.entity.Category;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.model.entity.Series;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank
    private String description;

    @NotNull
    private Integer brandId;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer seriesId;

    private List<ProductDetailRequest> productDetails;

}
