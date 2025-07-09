package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.ColorRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ColorResponse;
import com.ptithcm2021.laptopshop.service.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorController {
    private final ColorService colorService;

    @PostMapping("/create")
    public ApiResponse<ColorResponse>  createColor(@RequestBody @Valid ColorRequest colorRequest) {
        return ApiResponse.<ColorResponse>builder().data(colorService.addColor(colorRequest)).build();
    }

    @PostMapping("/update/{id}")
    public ApiResponse<ColorResponse>  updateColor(@PathVariable int id, @RequestBody @Valid ColorRequest colorRequest) {
        return ApiResponse.<ColorResponse>builder().data(colorService.updateColor(colorRequest, id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void>  deleteColor(@PathVariable int id) {
        colorService.deleteColor(id);
        return ApiResponse.<Void>builder().message("Deleted color successful").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ColorResponse>  getColor(@PathVariable int id) {
        return ApiResponse.<ColorResponse>builder().data(colorService.getColor(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<ColorResponse>> getAllColors() {
        return ApiResponse.<List<ColorResponse>>builder().data(colorService.getColors()).build();
    }

}
