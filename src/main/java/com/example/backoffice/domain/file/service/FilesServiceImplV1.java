package com.example.backoffice.domain.file.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.expense.entity.Expense;
import com.example.backoffice.domain.file.converter.FilesConverter;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.file.exception.FilesCustomException;
import com.example.backoffice.domain.file.exception.FilesExceptionCode;
import com.example.backoffice.domain.file.repository.FilesRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.awss3.S3Util;
import com.example.backoffice.global.exception.AWSCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesServiceImplV1 implements FilesServiceV1 {

    private final S3Util s3Util;
    private final FilesRepository filesRepository;

    // File을 올리는 이유? :
    // 1. board의 게시글 -> writer, board id는 가지고 있을 것
    // 2. member의 권한 변경 증빙 서류 -> writer는 있어야할 것
    @Override
    @Transactional
    public String createOneForMemberRole(MultipartFile file, Members member) {
        String originalFilename = file.getOriginalFilename();
        String uuidOriginalFilename = UUID.randomUUID() + "_" + originalFilename;
        Files image = FilesConverter.toEntityForMemberRole(uuidOriginalFilename, member);
        filesRepository.save(image);
        return s3Util.uploadFile(file);
    }

    @Override
    @Transactional
    public String createOneForBoard(MultipartFile file, Boards board) {
        String filename = s3Util.uploadFile(file);
        Files fileForBoard = FilesConverter.toEntityForBoards(filename, board);
        filesRepository.save(fileForBoard);
        return filename;
    }

    @Override
    @Transactional
    public String createOneForEvent(MultipartFile file, Events event) {
        String filename = s3Util.uploadFile(file);
        Files fileForBoard = FilesConverter.toEntityForEvents(filename, event);
        filesRepository.save(fileForBoard);
        return filename;
    }

    @Override
    @Transactional
    public Files createOneForExpense(
            MultipartFile file, Expense expense, Members loginMember){
        String filename = s3Util.uploadFile(file);
        Files fileForExpense
                = FilesConverter.toEntityForExpense(filename, expense, loginMember);
        filesRepository.save(fileForExpense);
        return fileForExpense;
    }

    @Override
    public String createImage(MultipartFile image) {
        return s3Util.uploadImage(image);
    }

    @Override
    @Transactional
    public void deleteForBoard(Long boardId, List<String> fileUrlList) {
        List<Files> files = filesRepository.findByBoardId(boardId);
        deleteForDomain(files, fileUrlList);
    }

    @Override
    @Transactional
    public void deleteForEvent(Long eventId, List<String> fileUrlList) {
        List<Files> files = filesRepository.findByEventId(eventId);
        deleteForDomain(files, fileUrlList);
    }

    private void deleteForDomain(List<Files> files, List<String> fileUrlList){
        for (String fileUrl : fileUrlList) {
            try {
                s3Util.removeFile(fileUrl);
            } catch (Exception e) {
                log.error("S3에서 파일 삭제 실패: " + fileUrl, e);
                throw new AWSCustomException(GlobalExceptionCode.AWS_S3_FILE_DELETE_FAIL);
            }
        }

        files.forEach(file -> {
            try {
                filesRepository.delete(file);
            } catch (Exception e) {
                log.error("데이터베이스에서 파일 엔티티 삭제 실패: " + file.getUrl(), e);
                throw new FilesCustomException(FilesExceptionCode.FILE_ENTITY_DELETE_FAIL);
            }
        });
    }

    @Override
    @Transactional
    public void deleteImage(String imageUrl) {
        filesRepository.deleteByUrl(imageUrl);
        s3Util.removeImage(imageUrl);
    }
}