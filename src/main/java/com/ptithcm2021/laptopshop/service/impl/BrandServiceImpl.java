package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.BrandMapper;
import com.ptithcm2021.laptopshop.model.dto.request.BrandRequest;
import com.ptithcm2021.laptopshop.model.dto.request.SeriesRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateBrandRequest;
import com.ptithcm2021.laptopshop.model.dto.response.BrandResponse;
import com.ptithcm2021.laptopshop.model.entity.Brand;
import com.ptithcm2021.laptopshop.repository.BrandRepository;
import com.ptithcm2021.laptopshop.service.BrandService;
import com.ptithcm2021.laptopshop.service.SeriesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final SeriesService seriesService;


    @Override
    @Caching(evict = @CacheEvict(value = "brands", key = "'all'"),
            put = @CachePut(value = "brands", key = "'brand:' + #result.id"))
    @Transactional
    public BrandResponse addBrand(BrandRequest brandRequest) {
        if (brandRepository.existsByName(brandRequest.getName())) {
            throw new AppException(ErrorCode.BRAND_NAME_EXISTED);
        }
        Brand brand = brandMapper.toBrand(brandRequest);
        BrandResponse response = brandMapper.toBrandResponse(brandRepository.save(brand));

        if (brandRequest.getSeriesRequests() != null) {
            response.setSeries(new ArrayList<>());
            for (SeriesRequest seriesRequest : brandRequest.getSeriesRequests()) {
                response.getSeries().add(seriesService.addSeries(seriesRequest, brand));
            }
        }
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    @Caching(evict = @CacheEvict(value = "brands", key = "'all'"),
            put = @CachePut(value = "brands", key = "'brand:' + #brandId"))
    @Transactional
    public BrandResponse updateBrand(UpdateBrandRequest brandRequest, int brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        if (!brand.getName().equals(brandRequest.getName())) {
            if (brandRepository.existsByName(brandRequest.getName())) {
                throw new AppException(ErrorCode.BRAND_NAME_EXISTED);
            }
        }

        if (brandRequest.getSeriesRequests() != null) {
            brandRequest.getSeriesRequests().forEach((id, seriesRequest) -> {
                seriesService.updateSeries(seriesRequest, id);
            });
        }
        brandMapper.updateBrand(brandRequest, brand);
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "brands", key = "'all'"),
            @CacheEvict(value = "brands", key = "'brand:' + #id")})
    public void deleteBrand(int id) {
        if(!brandRepository.existsById(id)) {
            throw new AppException(ErrorCode.BRAND_NOT_FOUND);
        }
        try {
            brandRepository.deleteById(id);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    @Cacheable(value = "brands", key = "'all'")
    public List<BrandResponse> getBrands() {
        return brandRepository.findAll().stream().map(brandMapper::toBrandResponse).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "brands", key = "'brand:' + #id")
    public BrandResponse getBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        return brandMapper.toBrandResponse(brand);
    }
}
