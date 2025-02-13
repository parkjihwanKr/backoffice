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
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file) {
        try {
            String filename = createUUIDFile(file);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, filename, file.getInputStream(), metadata);

            // pjhawss3buckect
            log.info(
                    "s3 서비스에 파일을 등록했습니다. : "
                            +amazonS3Client.getUrl(bucket, filename).toString());
            return amazonS3Client.getUrl(bucket, filename).toString();
        } catch (IOException e) {
            throw new AWSCustomException(GlobalExceptionCode.AWS_S3_FILE_UPLOAD_FAIL);
        }
    }

    public String uploadMemberProfile(MultipartFile file, Long memberId) {
        try {
            // 랜덤 UUID를 만듦.
            String filename = createUUIDFile(file);

            // s3 경로 설정(멤버마다 다른 위치의 프로필 사진을 가지고 있어야함.)
            String s3Key = String.format("members/%s/profiles/%s", memberId, filename);

            // 메타 데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // s3 버킷에 해당 이미지를 넣음.
            amazonS3Client.putObject(bucket, s3Key, file.getInputStream(), metadata);

            // 로그 출력 및 업로드된 파일 URL 반환
            return amazonS3Client.getUrl(bucket, s3Key).toString();
        } catch (IOException e) {
            throw new AWSCustomException(GlobalExceptionCode.AWS_S3_FILE_UPLOAD_FAIL);
        }
    }

    public void removeFile(String fileUrl) {
        try {
            // URL 디코딩
            String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
            // "_" 다음 문자부터 시작하여 파일 이름 추출
            int underscoreIndex = decodedUrl.lastIndexOf("/") + 1;
            String fileName = decodedUrl.substring(underscoreIndex);
            // S3에서 파일 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        } catch (Exception e) {
            throw new AWSCustomException(GlobalExceptionCode.AWS_S3_NOT_MATCHED_FILE_URL);
        }
    }

    public void removeImage(String imageUrl) {
        String imageName = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8)
                .substring(imageUrl.indexOf("_"));
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, imageName));
    }

    private String createUUIDFile(MultipartFile file){
        if (Objects.requireNonNull(file.getOriginalFilename()).isBlank()) {
            throw new AWSCustomException(GlobalExceptionCode.AWS_S3_FILE_NAME_IS_BLANK);
        }

        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + file.getOriginalFilename();
    }
}
