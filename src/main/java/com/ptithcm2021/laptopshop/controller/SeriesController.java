package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.SeriesRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.SeriesResponse;
import com.ptithcm2021.laptopshop.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {
    private final SeriesService seriesService;

    @PostMapping("/create")
    public ApiResponse<SeriesResponse> createSeries(@RequestBody SeriesRequest seriesRequest,
                                                    @RequestParam Integer brandId) {
        return ApiResponse.<SeriesResponse>builder().data(seriesService.addSeries(seriesRequest, brandId)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<SeriesResponse> updateSeries(@RequestBody SeriesRequest seriesRequest, @PathVariable Integer id) {
        return ApiResponse.<SeriesResponse>builder().data(seriesService.updateSeries(seriesRequest, id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSeries(@PathVariable Integer id) {
        seriesService.deleteSeries(id);
        return ApiResponse.<Void>builder().message("Deleted series successful").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SeriesResponse> getSeries(@PathVariable Integer id) {
        return ApiResponse.<SeriesResponse>builder().data(seriesService.getSeries(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<SeriesResponse>> getAllSeries() {
        return ApiResponse.<List<SeriesResponse>>builder().data(seriesService.getAllSeries()).build();
    }
}
