package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.CategoryRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CategoryResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CategoryService {
    CategoryResponse getCategory(int id);
    List<CategoryResponse> getCategories();

    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_CATEGORY', 'SCOPE_OWNER')")
    CategoryResponse createCategory(CategoryRequest request);

    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_CATEGORY', 'SCOPE_OWNER')")
    CategoryResponse updateCategory(CategoryRequest request, int id);

    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_CATEGORY', 'SCOPE_OWNER')")
    void deleteCategory(int id);
}
