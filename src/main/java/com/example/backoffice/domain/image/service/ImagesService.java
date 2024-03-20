package com.example.backoffice.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {
    public String uploadFile(MultipartFile file);
    public void removeProfileImageUrl(String profileImageUrl);
    public void removeFile(String imageUrl);
}
