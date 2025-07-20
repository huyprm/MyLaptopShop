package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.CategoryRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CategoryResponse;
import com.ptithcm2021.laptopshop.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

}
