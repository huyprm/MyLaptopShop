package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.ProductDetailMapper;
import com.ptithcm2021.laptopshop.mapper.ProductMapper;
import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductProjection;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductFilterRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.UpdateProductRequest;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

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

        productRepository.save(product);

        if (request.getProductDetails() != null) {
            request.getProductDetails().forEach(detail ->
                    productDetailService.createProductDetail(detail, product));
        }

        log.info("Product created successfully");

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
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

        if (request.getProductDetails() != null) {
            request.getProductDetails().forEach((aLong, productDetailRequest) ->{
                productDetailService.updateProductDetail(productDetailRequest, aLong);
            });
        }
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(long id) {
        if (!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        try{
            productRepository.deleteById(id);
        }catch (Exception e){
            log.error("Delete product failed: " + e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    public ProductResponse getProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toResponse(product);
    }

    @Override
    public PagedModel<ProductResponse> getProducts(Pageable pageable) {
        return new PagedModel<>(productRepository.findAll(pageable).map(productMapper::toResponse));
    }

    @Override
    public PagedModel<ItemProductProjection> getItemProducts(Pageable pageable) {
        return new PagedModel<>(productRepository.findOneProductDetailOfProductProjection(pageable));
    }

    @Override
    public PagedModel<ItemProductResponse> getItemProductsFilter(int size, int page, ProductFilterRequest request) {
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

        return new PagedModel<>(productDetailRepository.findAll(ProductDetailSpecification.filter(request), pageable)
                .map(productDetailMapper::toItemProductResponse));
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
