package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.CategoryMapper;
import com.ptithcm2021.laptopshop.model.dto.request.CategoryRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CategoryResponse;
import com.ptithcm2021.laptopshop.model.entity.Category;
import com.ptithcm2021.laptopshop.repository.CategoryRepository;
import com.ptithcm2021.laptopshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(value = "categories", key = "'category:' + #id")
    public CategoryResponse getCategory(int id) {
        Category category =categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Override
    @Caching(evict = @CacheEvict(value = "categories", key = "'all'", beforeInvocation = true),
            put = @CachePut(value = "categories", key = "'category:' + #result.id"))
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Caching(evict = @CacheEvict(value = "categories", key = "'all'", beforeInvocation = true),
            put = @CachePut(value = "categories", key = "'category:' + #id"))
    public CategoryResponse updateCategory(CategoryRequest request, int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->  new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "categories", key = "'all'"),
            @CacheEvict(value = "categories", key = "'category:' + #id")})
    public void deleteCategory(int id) {
        if(!categoryRepository.existsById(id)){
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        try {
            categoryRepository.deleteById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }
}
