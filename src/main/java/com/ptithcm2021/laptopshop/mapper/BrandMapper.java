package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.BrandRequest;
import com.ptithcm2021.laptopshop.model.dto.request.SeriesRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateBrandRequest;
import com.ptithcm2021.laptopshop.model.dto.response.BrandResponse;
import com.ptithcm2021.laptopshop.model.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResponse toBrandResponse(Brand brand);
    Brand toBrand(BrandRequest brandRequest);

    void updateBrand(UpdateBrandRequest brandRequest, @MappingTarget Brand brand);
}
