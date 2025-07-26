package com.ptithcm2021.laptopshop.model.dto.response;

import com.ptithcm2021.laptopshop.model.entity.Product;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    //private List<Product> products;
}
