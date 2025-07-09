package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    @PostMapping("/image")
    public ApiResponse<String> uploadAuto(@RequestParam MultipartFile file, @RequestParam (required = false) String currentImage) throws Exception {
        return ApiResponse.<String>builder().data(fileService.uploadFile(file, currentImage)).build();
    }


}
