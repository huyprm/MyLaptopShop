package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ConfigRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ConfigResponse;
import com.ptithcm2021.laptopshop.service.ConfigService;
import com.ptithcm2021.laptopshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configs")
@RequiredArgsConstructor
public class ConfigController {
    private final ConfigService configService;

    @PostMapping("/create")
    public ApiResponse<ConfigResponse> createConfig(@RequestBody ConfigRequest request){
        return ApiResponse.<ConfigResponse>builder().data(configService.createConfig(request)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ConfigResponse> updateConfig(@PathVariable long id, @RequestBody ConfigRequest request){
        return ApiResponse.<ConfigResponse>builder().data(configService.updateConfig(request,id)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteConfig(@PathVariable long id){
        configService.deleteConfig(id);
        return ApiResponse.<Void>builder().message("Deleted config successful").build();
    }


}
