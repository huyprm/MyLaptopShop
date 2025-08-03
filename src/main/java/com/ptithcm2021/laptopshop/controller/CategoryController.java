package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.CategoryRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.CategoryResponse;
import com.ptithcm2021.laptopshop.repository.CategoryRepository;
import com.ptithcm2021.laptopshop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder().data(categoryService.createCategory(request)).build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable int id, @RequestBody @Valid CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder().data(categoryService.updateCategory(request,id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable int id){
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder().message("Deleted category successful").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable int id){
        return ApiResponse.<CategoryResponse>builder().data(categoryService.getCategory(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<CategoryResponse>> getAllCategories(){
        return ApiResponse.<List<CategoryResponse>>builder().data(categoryService.getCategories()).build();
    }
}
