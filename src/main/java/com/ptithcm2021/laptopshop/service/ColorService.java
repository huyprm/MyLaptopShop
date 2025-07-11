package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.ColorRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ColorResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ColorService {
    ColorResponse getColor(int id);
    List<ColorResponse> getColors();

    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_CATEGORY', 'SCOPE_OWNER')")
    ColorResponse addColor(ColorRequest colorRequest);
    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_CATEGORY', 'SCOPE_OWNER')")
    ColorResponse updateColor(ColorRequest colorRequest, int id);
    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_CATEGORY', 'SCOPE_OWNER')")
    void deleteColor(int id);
}
