package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.CategoryRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getCategory(int id);
    List<CategoryResponse> getCategories();
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(CategoryRequest request, int id);
    void deleteCategory(int id);
}
