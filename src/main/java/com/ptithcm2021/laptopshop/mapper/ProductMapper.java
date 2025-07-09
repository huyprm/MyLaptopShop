package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductDetailMapper.class)
public interface ProductMapper {

    ProductResponse toResponse(Product product);
    Product toProduct(ProductRequest request);
}
