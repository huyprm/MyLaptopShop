package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.service.CacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    @CacheEvict(value = "products", key = "'product:' + #productDetailId")
    @Override
    public void evictSingleProduct(Long productDetailId) {
    }

}
