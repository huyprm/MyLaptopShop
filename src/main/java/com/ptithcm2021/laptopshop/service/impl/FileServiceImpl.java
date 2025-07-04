package com.ptithcm2021.laptopshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String currentImg) throws IOException {
        if(!currentImg.isEmpty()) {
            String publicId = extractPublicId(currentImg);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
        Map options = ObjectUtils.asMap("folder", "avatar");
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return uploadResult.get("url").toString();
    }

    private static String extractPublicId(String url) {
        url = url.replaceAll("^https://res.cloudinary.com/[^/]+/image/upload/v\\d+/", ""); // Xóa domain & version
        return url.replaceAll("\\.[a-z]+$", ""); // Xóa phần mở rộng (.jpg, .png, ...)
    }
}
