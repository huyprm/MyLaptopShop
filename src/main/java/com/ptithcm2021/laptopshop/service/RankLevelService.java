package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.RankLevelRequest;
import com.ptithcm2021.laptopshop.model.dto.response.RankLevelResponse;
import com.ptithcm2021.laptopshop.model.dto.response.RankUserResponse;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RankLevelService {
    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_RANKLEVEL', 'SCOPE_OWNER')")
    RankLevelResponse createRankLevel(RankLevelRequest request);
    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_RANKLEVEL', 'SCOPE_OWNER')")
    RankLevelResponse updateRankLevel(int id, RankLevelRequest request);

    void disableRankLevel(int id);

    RankLevelResponse getRankLevelById(int id);

    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_RANKLEVEL', 'SCOPE_OWNER')")
    void deleteRankLevel(int id);

    @PreAuthorize("hasAnyAuthority('SCOPE_PERM_RANKLEVEL', 'SCOPE_OWNER')")
    List<RankLevelResponse> getAllRankLevelsIsActive();

    RankUserResponse getRankLevelByUser();

    void loyaltyPointUpdate(String userId, int point);
}
