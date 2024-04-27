package com.example.backoffice.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {
    public String uploadFile(MultipartFile file);
    public String uploadImage(MultipartFile image);
    public void removeFile(String fileUrl);
    public void removeImage(String imageUrl);
}
