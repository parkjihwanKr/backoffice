package com.example.backoffice.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {
    public String uploadFile(MultipartFile file);
    public void removeFile(String profileImageUrl);
}
