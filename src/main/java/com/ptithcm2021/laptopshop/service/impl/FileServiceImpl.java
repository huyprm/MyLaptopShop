package com.ptithcm2021.laptopshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String currentImg) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        // Xoá ảnh cũ nếu có
        if (currentImg != null && !currentImg.isBlank()) {
            String publicId = extractPublicId(currentImg);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }

        Map<String, Object> options = ObjectUtils.asMap("folder", "avatar");

        long start = System.currentTimeMillis();
        log.info("⬆️ Uploading file: {} ({} bytes)", file.getOriginalFilename(), file.getSize());

        Map uploadResult;
        try (InputStream inputStream = file.getInputStream()) {
            uploadResult = cloudinary.uploader().upload(inputStream, options);
        }

        long duration = System.currentTimeMillis() - start;
        log.info("✅ Uploaded file in {} ms. Result URL: {}", duration, uploadResult.get("url"));

        return uploadResult.get("url").toString();
    }

    private static String extractPublicId(String url) {
        if (url == null) return "";
        // Xóa domain & version
        url = url.replaceAll("^https://res.cloudinary.com/[^/]+/image/upload/v\\d+/", "");
        // Xóa phần mở rộng (.jpg, .png, ...)
        return url.replaceAll("\\.[a-z]+$", "");
    }

}
