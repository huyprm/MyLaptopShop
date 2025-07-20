package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.ColorMapper;
import com.ptithcm2021.laptopshop.model.dto.request.ColorRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ColorResponse;
import com.ptithcm2021.laptopshop.model.entity.Color;
import com.ptithcm2021.laptopshop.repository.ColorRepository;
import com.ptithcm2021.laptopshop.service.ColorService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    @Override
    public ColorResponse getColor(int id) {
        Color color = colorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND));
        return colorMapper.toColorResponse(color);
    }

    @Override
    public List<ColorResponse> getColors() {
        return colorRepository.findAll().stream().map(colorMapper::toColorResponse).collect(Collectors.toList());
    }

    @Override
    public ColorResponse addColor(ColorRequest colorRequest) {
        if(colorRepository.existsByName(colorRequest.getName())){
            throw new AppException(ErrorCode.COLOR_NAME_EXISTED);
        }
        if (colorRepository.existsByHex(colorRequest.getHex())) {
            throw new AppException(ErrorCode.COLOR_HEX_EXISTED);
        }

        Color color = Color.builder()
                .hex(colorRequest.getHex())
                .name(colorRequest.getName())
                .build();
        return colorMapper.toColorResponse(colorRepository.save(color));
    }

    @Override
    public ColorResponse updateColor(ColorRequest colorRequest, int id) {
        if(colorRepository.existsByName(colorRequest.getName())){
            throw new AppException(ErrorCode.COLOR_NAME_EXISTED);
        }
        if (colorRepository.existsByHex(colorRequest.getHex())) {
            throw new AppException(ErrorCode.COLOR_HEX_EXISTED);
        }

        Color color = colorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND));
        return colorMapper.toColorResponse(colorRepository.save(color));
    }

    @Override
    public void deleteColor(int id) {
        if(!colorRepository.existsById(id)){
            throw new AppException(ErrorCode.COLOR_NOT_FOUND);
        }
        try {
            colorRepository.deleteById(id);
        }catch (Exception e){
            log.info(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }

    }
}
