package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductDetailMapper.class)
public interface ProductMapper {

    //@Mapping(source = "brand.id", target = "brandId")
    //@Mapping(source = "series.id", target = "seriesId")
    //@Mapping(source = "category.id", target = "categoryId")
    ProductResponse toResponse(Product product);
    Product toProduct(ProductRequest request);
}
