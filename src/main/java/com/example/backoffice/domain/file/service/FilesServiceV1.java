package com.example.backoffice.domain.file.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.expense.entity.Expense;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesServiceV1 {
    String createOneForMemberRole(MultipartFile file, Members member);

    String createOneForBoard(MultipartFile file, Boards board);

    /**
     * 멤버 프로필 이미지 생성
     * @param image : 해당 멤버 프로필 이미지
     * @param member : 파일을 생성하려는 멤버
     * @return : 해당 멤버의 랜덤으로 생성된 UUID 접두사 프로필 이미지 URL
     */
    String createMemberProfileImage(MultipartFile image, Members member);

    String createOneForEvent(MultipartFile file, Events event);

    Files createOneForExpense(
            MultipartFile file, Expense expense, Members loginMember);

    void deleteForBoard(Long boardId, List<String> fileList);

    void deleteForEvent(Long eventId, List<String> fileList);

    void deleteForExpense(Long expenseId, List<String> fileList);

    void deleteImage(String imageUrl);
}
