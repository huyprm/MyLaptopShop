package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.ConfigMapper;
import com.ptithcm2021.laptopshop.model.dto.request.Product.ConfigRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ConfigResponse;
import com.ptithcm2021.laptopshop.model.entity.Config;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.repository.ConfigRepository;
import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import com.ptithcm2021.laptopshop.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final ConfigMapper configMapper;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public ConfigResponse createConfig(ConfigRequest request) {
        if (request.getProductDetailId() == null) {
            throw new AppException(ErrorCode.PRODUCT_DETAIL_ID_NULL);
        }

        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return createConfigExternal(request, productDetail);
    }

    @Override
    public void createConfig(ConfigRequest request, ProductDetail productDetail) {

        createConfigExternal(request, productDetail);
    }

    @Override
    public ConfigResponse updateConfig(ConfigRequest request, long configId) {
        Config config = configRepository.findById(configId)
                .orElseThrow(() -> new AppException(ErrorCode.CONFIG_NOT_FOUND));
        configMapper.updateConfig(request, config);

        return configMapper.toResponse(configRepository.save(config));
    }

    @Override
    public void updateConfig(ConfigRequest request, Config config) {

        configMapper.updateConfig(request, config);

        configMapper.toResponse(configRepository.save(config));
    }

    @Override
    public void deleteConfig(long configId) {
        if(!configRepository.existsById(configId)) {
            throw new AppException(ErrorCode.CONFIG_NOT_FOUND);
        }
        try {
            configRepository.deleteById(configId);
        }catch (Exception e){
            log.error("Delete product detail failed: " + e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    private ConfigResponse createConfigExternal(ConfigRequest request, ProductDetail productDetail){
        Config config = configMapper.toConfig(request);
        config.setProductDetail(productDetail);
        configRepository.save(config);

        return configMapper.toResponse(config);
    }
}
