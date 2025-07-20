package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.BrandRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateBrandRequest;
import com.ptithcm2021.laptopshop.model.dto.response.BrandResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface BrandService {
    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_BRAND')")
    BrandResponse addBrand(BrandRequest brandRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_BRAND')")
    BrandResponse updateBrand(UpdateBrandRequest brandRequest, int brandId);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_BRAND')")
    void deleteBrand(int id);

    List<BrandResponse> getBrands();
    BrandResponse getBrand(int id);
}
