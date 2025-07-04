package com.ptithcm2021.laptopshop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file, String currentImg) throws IOException;
}
