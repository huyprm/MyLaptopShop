package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.ProductDetailMapper;
import com.ptithcm2021.laptopshop.mapper.ProductMapper;
import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductFilterRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.UpdateProductRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ItemProductResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductResponse;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.model.enums.SortedByEnum;
import com.ptithcm2021.laptopshop.model.enums.SortedDirectionEnum;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.ProductDetailService;
import com.ptithcm2021.laptopshop.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final SeriesRepository seriesRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDetailService productDetailService;
    private final ProductMapper productMapper;
    private final ProductDetailMapper productDetailMapper;
    private final ProductDetailRepository productDetailRepository;

    @Override
    @Transactional
    //@CachePut(value = "products", key = "'product:' + #result.id")
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() -> new AppException(ErrorCode.SERIES_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->  new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        product.setBrand(brand);
        product.setSeries(series);
        product.setCategory(category);

        ProductResponse response = productMapper.toResponse(productRepository.save(product));


        if (request.getProductDetailRequest() != null) {
            response.setProductDetails(new ArrayList<>());

            request.getProductDetailRequest().forEach(detail ->
                    response.getProductDetails().add(productDetailService.createProductDetail(detail, product)));
        }

        log.info("Product created successfully");

        return response;
    }

    @Override
    @Transactional
    //@CacheEvict(value = "products", key = "'product:'+ #productId", allEntries = false, beforeInvocation = true)
    public ProductResponse updateProduct(UpdateProductRequest request, long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->  new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (request.getBrandId() != null) {
            Series series = seriesRepository.findById(request.getSeriesId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERIES_NOT_FOUND));
            product.setSeries(series);
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() ->   new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            product.setCategory(category);
        }

        if(request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() ->  new AppException(ErrorCode.BRAND_NOT_FOUND));
            product.setBrand(brand);
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getProductDetailRequest() != null) {
            request.getProductDetailRequest().forEach((productDetailRequest) ->{
                productDetailService.updateProductDetail(productDetailRequest, product);
            });
        }
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    //@CacheEvict(value = "products", key = "'product:' + #id")
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        try{
            productRepository.delete(product);
        }catch (Exception e){
            log.error("Delete product failed: " + e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    //@Cacheable(value = "products",key = "'product:' + #id")
    public ProductResponse getProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toResponse(product);
    }

    @Override
    public PagedModel<ProductResponse> getProducts(Pageable pageable, String keyword, Integer brandId) {
        return new PagedModel<>(productRepository.searchByKeywordAndBrand(keyword, brandId, pageable).map(productMapper::toResponse));
    }

    @Override
    //@Cacheable(value = "products", key = "'page:' + #pageable.getPageNumber()", condition ="#pageable.pageNumber == 0" )
    public PageWrapper<ItemProductProjection> getItemProducts(Pageable pageable) {
        Page<ItemProductProjection> page = productRepository.findOneProductDetailOfProductProjection(pageable);
        return PageWrapper.<ItemProductProjection>builder()
                .content(page.getContent().stream().toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
//    @Cacheable(
//            value = "products",
//            key = "'filterPage:' + #page + ':' + T(org.apache.commons.lang3.builder.HashCodeBuilder).reflectionHashCode(#request)",
//            condition = "#page == 0"
//    )

    public PageWrapper<ItemProductResponse> getItemProductsFilter(int size, int page, ProductFilterRequest request) {
        String sortProperty = "createdDate";
        Sort.Direction sortDirection = Sort.Direction.DESC;

        if (request.getSortBy() != null) {
            sortProperty = switch (request.getSortBy()) {
                case SortedByEnum.PRICE -> "discountPrice";
                case SortedByEnum.RATING -> "totalRating";
                case SortedByEnum.NEW -> "createdDate";
                case SortedByEnum.SOLD -> "soldQuantity";
            };
        }
        if (SortedDirectionEnum.ASC.name().equalsIgnoreCase(String.valueOf(request.getSortDirection()))) {
            sortDirection = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortProperty)
        );

        List<Long> matchedIds;
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            matchedIds = productDetailRepository.findAllProductDetailIdsByKeyword(request.getKeyword());
        } else {
            matchedIds = null;
        }

        Specification<ProductDetail> spec = ProductDetailSpecification.filter(request);

        if (matchedIds != null) {
            spec = spec.and((root, query, cb) -> root.get("id").in(matchedIds));
        }

        Page<ItemProductResponse> result = productDetailRepository.findProducts(spec, pageable);


        return PageWrapper.<ItemProductResponse>builder()
                .content(result.getContent())
                .totalElements(result.getTotalElements())
                .pageSize(result.getSize())
                .pageNumber(result.getNumber())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    public PagedModel<ItemProductProjection> searchProduct(String keyword, Pageable pageable) {
        return new PagedModel<>(productRepository.searchFullText(keyword, pageable));
    }

    @Override
    public List<String> getSortOptions() {
        return Arrays.stream(SortedByEnum.values()).map(SortedByEnum::getLabel).collect(Collectors.toList());
    }


}
