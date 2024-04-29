package com.example.backoffice.domain.file.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.file.converter.FilesConverter;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.file.repository.FilesRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.awss3.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {

    private final S3Util s3Util;
    private final FilesRepository filesRepository;

    // File을 올리는 이유? :
    // 1. board의 게시글 -> writer, board id는 가지고 있을 것
    // 2. member의 권한 변경 증빙 서류 -> writer는 있어야할 것
    @Override
    public String createFileForMemberRole(MultipartFile file, Members member){
        String originalFilename = file.getOriginalFilename();
        String uuidOriginalFilename = UUID.randomUUID()+"_"+originalFilename;
        Files image = FilesConverter.toEntityForMemberRole(uuidOriginalFilename, member);
        filesRepository.save(image);
        return s3Util.uploadFile(file);
    }

    @Override
    public String createFileForBoard(MultipartFile file, Boards board){
        String filename = s3Util.uploadFile(file);
        Files fileForBoard = FilesConverter.toEntityForBoards(filename, board);
        filesRepository.save(fileForBoard);
        return filename;
    }

    @Override
    public String createImage(MultipartFile image){
        return s3Util.uploadImage(image);
    }

    @Override
    public void deleteFile(String fileUrl){
        s3Util.removeFile(fileUrl);
    }

    @Override
    public void deleteImage(String imageUrl){
        s3Util.removeImage(imageUrl);
    }
}