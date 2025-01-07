package com.example.backoffice.domain.file.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesServiceV1 {

    /**
     * 멤버의 권한 변경을 위한 파일 생성
     * @param file : 해당하는 파일
     * @param member : 해당 파일 작성자
     * @return : 해당하는 파일 URL
     */
    String createOneForMemberRole(MultipartFile file, Members member);

    /**
     * 게시글 파일 등록을 위한 파일 생성
     * @param file : 해당하는 파일
     * @param board : 파일을 등록할 게시글
     * @return : 해당하는 파일 URL
     */
    String createOneForBoard(MultipartFile file, Boards board);

    /**
     * 멤버 프로필 이미지 생성
     * @param image : 해당 멤버 프로필 이미지
     * @param member : 파일을 생성하려는 멤버
     * @return : 해당 멤버의 랜덤으로 생성된 UUID 접두사 프로필 이미지 URL
     */
    String createMemberProfileImage(MultipartFile image, Members member);

    /**
     * 일정을 위한 파일 생성
     * @param file : 해당하는 파일
     * @param event : 파일을 등록할 일정
     * @return : 해당하는 파일 URL
     */
    String createOneForEvent(MultipartFile file, Events event);

    /**
     * 해당하는 게시글의 파일 삭제
     * @param boardId : 해당하는 게시글 아이디
     * @param fileList : 게시글에 존재하는 파일 리스트
     */
    void deleteForBoard(Long boardId, List<String> fileList);

    /**
     * 해당하는 일정의 파일 삭제
     * @param eventId : 해당하는 일정 아이디
     * @param fileList : 일정에 존재하는 파일 리스트
     */
    void deleteForEvent(Long eventId, List<String> fileList);

    /**
     * 이미지 URL을 통한 파일 삭제
     * @param imageUrl : 해당하는 이미지 URL
     */
    void deleteImage(String imageUrl);
}
