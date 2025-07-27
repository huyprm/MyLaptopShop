package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.repository.InventoryRepository;
import com.ptithcm2021.laptopshop.service.InventoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/serial-numbers/{productDetailId}")
    public ApiResponse<List<String>> getSerialNumbersByProductDetailId(@PathVariable Long productDetailId) {
        List<String> serialNumbers = inventoryService.getSerialNumbersByProductDetailIdAndActive(productDetailId);
        return ApiResponse.<List<String>>builder()
                .message("Fetched serial numbers successfully")
                .data(serialNumbers)
                .build();
    }
}
