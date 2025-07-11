package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.BrandRequest;
import com.ptithcm2021.laptopshop.model.dto.request.UpdateBrandRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.BrandResponse;
import com.ptithcm2021.laptopshop.service.BrandService;
import com.ptithcm2021.laptopshop.service.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping("/create")
    public ApiResponse<BrandResponse> createBrand(@RequestBody @Valid BrandRequest brandRequest) {
        return ApiResponse.<BrandResponse>builder().data(brandService.addBrand(brandRequest)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<BrandResponse> updateBrand(@RequestBody @Valid UpdateBrandRequest brandRequest, @PathVariable int id) {
        return ApiResponse.<BrandResponse>builder().data(brandService.updateBrand(brandRequest, id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<BrandResponse> deleteBrand(@PathVariable int id) {
        brandService.deleteBrand(id);
        return ApiResponse.<BrandResponse>builder().message("Deleted brand successful").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> getBrandById(@PathVariable int id) {
        return ApiResponse.<BrandResponse>builder().data(brandService.getBrand(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<BrandResponse>> getAllBrands() {
        return ApiResponse.<List<BrandResponse>>builder().data(brandService.getBrands()).build();
    }
}
