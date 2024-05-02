package com.example.backoffice.domain.file.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesService {
    String createFileForMemberRole(MultipartFile file, Members member);
    String createFileForBoard(MultipartFile file, Boards board);
    String createImage(MultipartFile image);
    void deleteFile(Long boardId, List<String> fileList);
    void deleteImage(String imageUrl);
}
