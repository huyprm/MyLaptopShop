package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.RankLevelRequest;
import com.ptithcm2021.laptopshop.model.dto.response.RankLevelResponse;
import com.ptithcm2021.laptopshop.model.dto.response.RankUserResponse;

import java.util.List;

public interface RankLevelService {
    RankLevelResponse createRankLevel(RankLevelRequest request);
    RankLevelResponse updateRankLevel(int id, RankLevelRequest request);

    void disableRankLevel(int id);

    RankLevelResponse getRankLevelById(int id);
    void deleteRankLevel(int id);

    List<RankLevelResponse> getAllRankLevelsIsActive();

    RankUserResponse getRankLevelByUser();
}
