package com.example.backoffice.domain.file.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    public String createFileForMemberRole(MultipartFile file, Members member);
    public String createFileForBoard(MultipartFile file, Boards board);
    public String createImage(MultipartFile image);
    public void deleteFile(String fileUrl);
    public void deleteImage(String imageUrl);
}
