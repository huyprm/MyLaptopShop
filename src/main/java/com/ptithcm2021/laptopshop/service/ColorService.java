package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.ColorRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ColorResponse;

import java.util.List;

public interface ColorService {
    ColorResponse getColor(int id);
    List<ColorResponse> getColors();
    ColorResponse addColor(ColorRequest colorRequest);
    ColorResponse updateColor(ColorRequest colorRequest, int id);
    void deleteColor(int id);
}
