package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.ProductDetailMapper;
import com.ptithcm2021.laptopshop.mapper.ProductMapper;
import com.ptithcm2021.laptopshop.model.dto.projection.ItemProductDetailProjection;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Product.UpdateProductDetailRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ProductDetailResponse;
import com.ptithcm2021.laptopshop.model.entity.Color;
import com.ptithcm2021.laptopshop.model.entity.Inventory;
import com.ptithcm2021.laptopshop.model.entity.Product;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.repository.ColorRepository;
import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import com.ptithcm2021.laptopshop.repository.ProductRepository;
import com.ptithcm2021.laptopshop.service.ConfigService;
import com.ptithcm2021.laptopshop.service.ProductDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final ProductDetailMapper productDetailMapper;
    private final ConfigService configService;
    private final ColorRepository colorRepository;
    private final ProductMapper productMapper;

    @Override
    @CachePut(value = "products", key = "'product:' + #productDetailRequest.productId")
    @Transactional
    public ProductDetailResponse createProductDetail(ProductDetailRequest productDetailRequest) {

        if(productDetailRequest.getProductId() == null) {
            throw new AppException(ErrorCode.PRODUCT_ID_NULL);
        }
        Product product = productRepository.findById(productDetailRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));


        return createProductDetailInternal(productDetailRequest, product);
    }

    @Override
    public ProductDetailResponse createProductDetail(ProductDetailRequest productDetailRequest, Product product) {

        return createProductDetailInternal(productDetailRequest, product);
    }

    @Override
    @CacheEvict(value = "products", key = "'product:' + #product.id", beforeInvocation = true)
    @Transactional
    public void updateProductDetail(UpdateProductDetailRequest request, Product product) {
        ProductDetail productDetail = productDetailRepository.findById(request.getId())
                .orElse(null);

        if (productDetail == null) {
            ProductDetailRequest productDetailRequest = ProductDetailRequest.builder()
                    .colorId(request.getColorId())
                    .configRequest(request.getConfigRequest())
                    .slug(request.getSlug())
                    .originalPrice(request.getOriginalPrice())
                    .thumbnail(request.getThumbnail())
                    .warrantyProd(request.getWarrantyProd())
                    .images(request.getImages())
                    .title(request.getTitle())
                    .build();

            createProductDetailInternal(productDetailRequest, product);
            return;
        }

        if ( productDetail.getProduct().getId() != product.getId()) {
            throw new AppException(ErrorCode.PRODUCT_DETAIL_NOT_BELONG_TO_PRODUCT);
        }
        
        productDetailMapper.updateProductDetail(request, productDetail);

        if (request.getConfigRequest() != null) {
            configService.updateConfig(request.getConfigRequest(), productDetail.getConfig());
        }

        productDetailRepository.save(productDetail);
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProductDetail(long productDetailId) {
        if (!productDetailRepository.existsById(productDetailId)) {
            throw  new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        try{
            productDetailRepository.deleteById(productDetailId);
        }catch (Exception e){
            log.error("Delete product detail failed: " + e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    public void handleAddRating(Long productDetailId, int rating) {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        int totalReviews = productDetail.getTotalReviews() + 1;
        double newTotalRating = ((productDetail.getTotalRating() * productDetail.getTotalReviews()) + rating) / totalReviews;

        productDetail.setTotalRating(newTotalRating);
        productDetail.setTotalReviews(totalReviews);

        productDetailRepository.save(productDetail);

        log.info("Product detail rating updated successfully for ID: {}", productDetailId);
    }

    @Override
    public void handleUpdateRating(Long productDetailId, int oldRating, int newRating) {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        int totalReviews = productDetail.getTotalReviews();
        double newTotalRating = ((productDetail.getTotalRating() * productDetail.getTotalReviews()) - oldRating + newRating) / totalReviews;

        productDetail.setTotalRating(newTotalRating);

        productDetailRepository.save(productDetail);
        log.info("Product detail rating updated successfully for ID: {}", productDetailId);
    }

    @Override
    public void handleDeleteRating(Long productDetailId, int rating) {

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        int totalReviews = productDetail.getTotalReviews() - 1;

        if (totalReviews < 0) {
            return;
        } if (totalReviews > 0){
            double newTotalRating = ((productDetail.getTotalRating() * productDetail.getTotalReviews()) - rating) / totalReviews;

            productDetail.setTotalRating(newTotalRating);
            productDetail.setTotalReviews(totalReviews);
        } else {
            double newTotalRating = (productDetail.getTotalRating() * productDetail.getTotalReviews()) - rating;
            productDetail.setTotalRating(newTotalRating);
            productDetail.setTotalReviews(0);
        }

        productDetailRepository.save(productDetail);

        log.info("Product detail rating updated successfully for ID: {}", productDetailId);
    }

    @Override
    public PageWrapper<ItemProductDetailProjection> getProductDetails(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<ItemProductDetailProjection> result = productDetailRepository
                .searchProductDetails(keyword, pageable);

        return PageWrapper.<ItemProductDetailProjection>builder()
                .content(result.getContent())
                .pageNumber(result.getNumber())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    private ProductDetailResponse createProductDetailInternal(ProductDetailRequest productDetailRequest, Product product) {
        ProductDetail productDetail = productDetailMapper.toProductDetail(productDetailRequest);
        productDetail.setProduct(product);

        Color color = colorRepository.findById(productDetailRequest.getColorId())
                .orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND));

        productDetail.setColor(color);
        productDetail.setInventory(
                Inventory.builder()
                        .productDetail(productDetail)
                        .quantity(0)
                        .build());

        ProductDetailResponse response = productDetailMapper
                .toResponse(productDetailRepository.save(productDetail));

        if (productDetailRequest.getConfigRequest() != null){

            response.setConfig(configService.createConfig(productDetailRequest.getConfigRequest(), productDetail));
        }

        log.info("Product detail created successfully");

        return response;
    }

}
