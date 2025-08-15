package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.RankLevelRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.RankLevelResponse;
import com.ptithcm2021.laptopshop.model.dto.response.RankUserResponse;
import com.ptithcm2021.laptopshop.service.RankLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rank-levels")
public class RankLevelController {
    private final RankLevelService rankLevelService;

    @PostMapping("/create")
    public ApiResponse<RankLevelResponse> createRankLevel(@RequestBody @Valid RankLevelRequest request) {
        RankLevelResponse response = rankLevelService.createRankLevel(request);
        return ApiResponse.<RankLevelResponse>builder()
                .data(response)
                .message("Rank level created successfully")
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<RankLevelResponse> updateRankLevel(@PathVariable int id, @RequestBody @Valid RankLevelRequest request) {
        RankLevelResponse response = rankLevelService.updateRankLevel(id, request);
        return ApiResponse.<RankLevelResponse>builder()
                .data(response)
                .message("Rank level updated successfully")
                .build();
    }

//    @PutMapping("/disable/{id}")
//    public ApiResponse<String> disableRankLevel(@PathVariable int id) {
//        rankLevelService.disableRankLevel(id);
//        return ApiResponse.<String>builder()
//                .message("Rank level disabled successfully")
//                .build();
//    }

    @GetMapping("/{id}")
    public ApiResponse<RankLevelResponse> getRankLevelById(@PathVariable int id) {
        RankLevelResponse response = rankLevelService.getRankLevelById(id);
        return ApiResponse.<RankLevelResponse>builder()
                .data(response)
                .message("Rank level retrieved successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRankLevel(@PathVariable int id) {
        rankLevelService.deleteRankLevel(id);
        return ApiResponse.<String>builder()
                .message("Rank level deleted successfully")
                .build();
    }

    @GetMapping("/active")
    public ApiResponse<List<RankLevelResponse>> getAllActiveRankLevels() {
        List<RankLevelResponse> responses = rankLevelService.getAllRankLevelsIsActive();
        return ApiResponse.<List<RankLevelResponse>>builder()
                .data(responses)
                .message("Active rank levels retrieved successfully")
                .build();
    }

    @GetMapping("/user")
    public ApiResponse<RankUserResponse> getRankLevelByUser() {
        RankUserResponse response = rankLevelService.getRankLevelByUser();
        return ApiResponse.<RankUserResponse>builder()
                .data(response)
                .message("User's rank level retrieved successfully")
                .build();
    }

}
