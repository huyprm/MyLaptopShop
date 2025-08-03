package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.SeriesMapper;
import com.ptithcm2021.laptopshop.model.dto.request.SeriesRequest;
import com.ptithcm2021.laptopshop.model.dto.response.SeriesResponse;
import com.ptithcm2021.laptopshop.model.entity.Brand;
import com.ptithcm2021.laptopshop.model.entity.Series;
import com.ptithcm2021.laptopshop.repository.BrandRepository;
import com.ptithcm2021.laptopshop.repository.SeriesRepository;
import com.ptithcm2021.laptopshop.service.SeriesService;
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
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;
    private final BrandRepository brandRepository;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "brands", key = "'all'"), @CacheEvict(value = "series", key = "'all'")},
            put = @CachePut(value = "series", key = "#result.id"))
    public SeriesResponse addSeries(SeriesRequest seriesRequest, int brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() ->  new AppException(ErrorCode.BRAND_NOT_FOUND));
        return addSeriesInternal(seriesRequest, brand);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "series", key = "'all'")},
            put = @CachePut(value = "series", key = "#result.id"))
    public SeriesResponse addSeries(SeriesRequest seriesRequest, Brand brand) {
        return addSeriesInternal(seriesRequest, brand);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "brands", key = "'all'"), @CacheEvict(value = "series", key = "'all'")},
            put = @CachePut(value = "series", key = "#seriesId"))
    public SeriesResponse updateSeries(SeriesRequest seriesRequest, int seriesId) {
        Series series= seriesRepository.findById(seriesId)
                .orElseThrow(() ->   new AppException(ErrorCode.SERIES_NOT_FOUND));
        seriesMapper.updateSeries(seriesRequest, series);
        return seriesMapper.toSeriesResponse(seriesRepository.save(series));
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "brands", key = "'all'"), @CacheEvict(value = "series", key = "'all'")})
    public void deleteSeries(int seriesId) {
        if(!seriesRepository.existsById(seriesId)) {
            throw new AppException(ErrorCode.SERIES_NOT_FOUND);
        }
        try {
            seriesRepository.deleteById(seriesId);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    @Cacheable(value = "series", key = "#seriesId")
    public SeriesResponse getSeries(int seriesId) {
        Series series= seriesRepository.findById(seriesId)
                .orElseThrow(() ->   new AppException(ErrorCode.SERIES_NOT_FOUND));
        return seriesMapper.toSeriesResponse(series);
    }

    @Override
    @Cacheable(value = "series", key = "'all'")
    public List<SeriesResponse> getAllSeries() {
        return seriesRepository.findAll().stream().map(seriesMapper::toSeriesResponse).collect(Collectors.toList());
    }

    private SeriesResponse addSeriesInternal(SeriesRequest seriesRequest, Brand brand) {
        Series series = seriesMapper.toSeries(seriesRequest);
        series.setBrand(brand);
        return seriesMapper.toSeriesResponse(seriesRepository.save(series));
    }

}
