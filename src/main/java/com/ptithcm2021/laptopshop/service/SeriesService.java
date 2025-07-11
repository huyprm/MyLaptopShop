package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.SeriesRequest;
import com.ptithcm2021.laptopshop.model.dto.response.SeriesResponse;
import com.ptithcm2021.laptopshop.model.entity.Brand;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface SeriesService {
    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SERIES')")
    SeriesResponse addSeries(SeriesRequest seriesRequest, int brandId);
    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SERIES')")
    SeriesResponse addSeries(SeriesRequest seriesRequest, Brand brand);
    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SERIES')")
    SeriesResponse updateSeries(SeriesRequest seriesRequest, int seriesId);
    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_SERIES')")
    void deleteSeries(int seriesId);
    SeriesResponse getSeries(int seriesId);
    List<SeriesResponse> getAllSeries();
}
