package com.ptithcm2021.laptopshop.service;

import org.springframework.cache.annotation.CacheEvict;

public interface CacheService {
    void evictSingleProduct(Long productDetailId);
}
