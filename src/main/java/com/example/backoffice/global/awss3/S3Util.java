package com.example.backoffice.global.awss3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.backoffice.global.exception.AWSCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${YOUR_BUCKET_NAME}")
    private String bucket;

    public String uploadImage(MultipartFile image){
        return uploadFileOrImage(image);
    }

    public String uploadFile(MultipartFile file){
        return uploadFileOrImage(file);
    }

    public void removeFile(String fileUrl) {
        String fileName = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8)
                .substring(fileUrl.indexOf("_"));
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    public void removeImage(String imageUrl){
        String imageName = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8)
                .substring(imageUrl.indexOf("_"));
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, imageName));
    }

    private String uploadFileOrImage(MultipartFile file){
        try{
            if (Objects.requireNonNull(file.getOriginalFilename()).isBlank()) {
                throw new AWSCustomException(GlobalExceptionCode.AWS_S3_FILE_NAME_IS_BLANK);
            }
            String uuid = UUID.randomUUID().toString();
            String filename = uuid+"_"+file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, filename, file.getInputStream(), metadata);

            return amazonS3Client.getUrl(bucket, filename).toString();
        }catch(IOException e){
            throw new AWSCustomException(GlobalExceptionCode.AWS_S3_FILE_UPLOAD_FAIL);
        }
    }
}
