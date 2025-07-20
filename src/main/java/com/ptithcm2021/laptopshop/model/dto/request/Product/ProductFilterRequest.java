package com.ptithcm2021.laptopshop.model.dto.request.Product;

import com.ptithcm2021.laptopshop.model.enums.SortedByEnum;
import com.ptithcm2021.laptopshop.model.enums.SortedDirectionEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {
    private Integer brandId;
    private Integer categoryId;
    private Integer seriesId;
    private Integer minPrice;
    private Integer maxPrice;
    private String ram;
    private String hardDrive;
    private String cpu;
    private SortedByEnum sortBy;
    private SortedDirectionEnum sortDirection =  SortedDirectionEnum.DESC;
}
