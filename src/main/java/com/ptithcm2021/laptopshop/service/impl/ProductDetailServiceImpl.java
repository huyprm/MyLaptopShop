package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.ProductDetailMapper;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ProductDetailRequest;
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
    @CacheEvict(value = "products", key = "'product:' + #productDetailRequest.productId", beforeInvocation = true)
    @Transactional
    public ProductDetailResponse updateProductDetail(ProductDetailRequest productDetailRequest, long productDetailId) {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() ->  new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productDetailMapper.updateProductDetail(productDetailRequest, productDetail);

        if (productDetailRequest.getConfigRequest() != null) {
            configService.updateConfig(productDetailRequest.getConfigRequest(), productDetail.getConfig());
        }

        return productDetailMapper.toResponse(productDetailRepository.save(productDetail));
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

    private ProductDetailResponse createProductDetailInternal(ProductDetailRequest productDetailRequest, Product product) {
        ProductDetail productDetail = productDetailMapper.toProductDetail(productDetailRequest);
        productDetail.setProduct(product);

        Color color = colorRepository.findById(productDetailRequest.getColorId())
                .orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND));

        productDetail.setColor(color);
        productDetail.setInventory(new Inventory());

        ProductDetailResponse response = productDetailMapper
                .toResponse(productDetailRepository.save(productDetail));

        if (productDetailRequest.getConfigRequest() != null){

            response.setConfig(configService.createConfig(productDetailRequest.getConfigRequest(), productDetail));
        }

        log.info("Product detail created successfully");

        return response;
    }
}
