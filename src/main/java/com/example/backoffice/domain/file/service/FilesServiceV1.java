package com.example.backoffice.domain.file.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesServiceV1 {
    String createOneForMemberRole(MultipartFile file, Members member);

    String createOneForBoard(MultipartFile file, Boards board);

    String createImage(MultipartFile image);

    void delete(Long boardId, List<String> fileList);

    void deleteImage(String imageUrl);
}
