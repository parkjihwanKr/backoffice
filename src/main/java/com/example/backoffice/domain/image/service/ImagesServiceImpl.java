package com.example.backoffice.domain.image.service;

import com.example.backoffice.global.awss3.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesService {

    private final S3Util s3Util;

    @Override
    public String uploadFile(MultipartFile file){
        return s3Util.uploadFile(file);
    }

    @Override
    public void removeFile(String profileImageUrl){

    }
}