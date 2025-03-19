package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardsServiceV1 {

    /**
     * 중요 표시된 게시글을 수정일 기준으로 내림차순으로 조회
     *
     * @param boardType 게시글 타입 (예: 전체 게시판, 부서 게시판 등)
     * @return 중요 표시된 게시글 리스트
     */
    List<Boards> findByIsImportantTrueAndBoardTypeOrderByModifiedAtDesc(BoardType boardType);

    /**
     * 중요하지 않은 게시글을 생성일 기준으로 내림차순으로 페이징하여 조회
     *
     * @param pageable 페이징 정보
     * @param boardType 게시글 타입
     * @return 중요하지 않은 게시글의 페이지 객체
     */
    Page<Boards> findByIsImportantFalseAndBoardTypeOrderByCreatedAtDesc(Pageable pageable, BoardType boardType);

    /**
     * 게시글의 댓글 수를 반환
     *
     * @param board 댓글 수를 계산할 게시글
     * @return 게시글의 댓글 수
     */
    Long getCommentListSize(Boards board);

    /**
     * 부서와 전체 게시글 타입에 따라 게시글을 페이징하여 조회
     *
     * @param pageable 페이징 정보
     * @param department 부서 이름
     * @param boardType 게시글 타입
     * @return 해당 부서와 게시글 타입의 페이지 객체
     */
    Page<Boards> findAllByDepartmentAndBoardType(Pageable pageable, MemberDepartment department, BoardType boardType);

    /**
     * 게시글 저장
     *
     * @param board 저장할 게시글
     * @return 저장된 게시글
     */
    Boards save(Boards board);

    /**
     * 부서와 게시글 ID로 게시글 조회
     *
     * @param boardId 게시글 ID
     * @param department 부서 이름
     * @return 해당 게시글
     * @throws BoardsCustomException {@link BoardsExceptionCode#NOT_FOUND_BOARD} 게시글이 존재하지 않을 경우 예외 발생
     */
    Boards findByIdAndDepartment(Long boardId, MemberDepartment department);

    /**
     * 게시글 ID로 게시글 조회
     *
     * @param boardId 게시글 ID
     * @return 해당 게시글
     * @throws BoardsCustomException {@link BoardsExceptionCode#NOT_FOUND_BOARD} 게시글이 존재하지 않을 경우 예외 발생
     */
    Boards findById(Long boardId);

    /**
     * 게시글 ID로 게시글 삭제
     *
     * @param boardId 삭제할 게시글 ID
     * @throws BoardsCustomException 게시글이 존재하지 않을 경우 예외 발생
     */
    void deleteById(Long boardId);

    /**
     * 게시글 타입에 따른 게시글 3개 조회
     * @param boardType : 게시글 타입
     * @return 게시글 타입에 따른 게시글 3개
     */
    List<Boards> findThreeByCreatedAtDesc(BoardType boardType);

    /**
     * 메인 페이지의 전체 게시글 상단의 3개 조회
     * @param loginMember : 로그인 멤버
     * @return 메인 페이지의 전체 게시글 상단의 3개 반환
     */
    List<BoardsResponseDto.ReadSummarizedOneDto> getGeneralBoardDtoList(
            Members loginMember);

    /**
     * 메인 페이지의 부서 게시글 상단의 3개 조회
     * @param loginMember : 로그인 멤버
     * @return 메인 페이지의 부서 게시글 상단의 3개 반환
     */
    List<BoardsResponseDto.ReadSummarizedOneDto> getDepartmentBoardDtoList(
            Members loginMember);
}
