package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.RankLevelRequest;
import com.ptithcm2021.laptopshop.model.dto.response.RankLevelResponse;
import com.ptithcm2021.laptopshop.model.entity.RankLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RankLevelMapper {
     RankLevelResponse toResponse(RankLevel rankLevel);
     RankLevel toRankLevel(RankLevelRequest request);

     void updateRankLevel(RankLevelRequest request, @MappingTarget RankLevel rankLevel);
}
