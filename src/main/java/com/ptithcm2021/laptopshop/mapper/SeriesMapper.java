package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.SeriesRequest;
import com.ptithcm2021.laptopshop.model.dto.response.SeriesResponse;
import com.ptithcm2021.laptopshop.model.entity.Product;
import com.ptithcm2021.laptopshop.model.entity.Series;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SeriesMapper {
    SeriesResponse toSeriesResponse(Series series);

    Series toSeries(SeriesRequest seriesRequest);

    void updateSeries(SeriesRequest seriesRequest, @MappingTarget Series series);

}
