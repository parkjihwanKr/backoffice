package com.example.backoffice.domain.board.facade;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardsServiceFacadeV1 {

    /**
     * 게시글 모두 조회
     * @param pageable 페이징 처리 정보 (페이지 번호, 크기 등)
     * @return 페이징된 게시글 목록
     *         - 중요 및 잠금 상태에 따라 상위 3개의 고정 게시글 포함
     *         - 반환되는 게시글 목록은 {@link BoardsResponseDto.ReadAllDto}로 구성됨
     */
    Page<BoardsResponseDto.ReadAllDto> readPageGeneralType(Pageable pageable);

    /**
     * 게시글 상세 조회
     * @param boardId 조회하려는 게시글의 고유 식별 ID
     * @param loginMember 로그인 멤버
     * @return 조회된 게시글의 세부 정보를 포함한 DTO
     *         - 게시글 정보, 작성자, 반응, 댓글 등 포함
     *         - 반환 타입: {@link BoardsResponseDto.ReadOneDto}
     */
    BoardsResponseDto.ReadOneDto readOneGeneralType(Long boardId, Members loginMember);

    /**
     * 게시글 생성
     *
     * @param loginMember 게시글 생성 요청을 한 로그인 사용자
     * @param requestDto 게시글 생성에 필요한 요청 데이터 {@link BoardsRequestDto.CreateOneDto}
     * @param files 첨부 파일 리스트
     * @return 생성된 게시글의 정보 {@link BoardsResponseDto.CreateOneDto}
     */
    BoardsResponseDto.CreateOneDto createOneGeneralType(
            Members loginMember, BoardsRequestDto.CreateOneDto requestDto,
            List<MultipartFile> files);

    /**
     * 게시글 수정
     *
     * @param boardId 수정하려는 게시글의 고유 ID
     * @param loginMember 요청을 수행한 로그인 사용자
     * @param requestDto 수정 요청 데이터 {@link BoardsRequestDto.UpdateOneDto}
     * @param files 첨부 파일 리스트
     * @return 수정된 게시글의 정보
     *         - 게시글 ID, 제목, 내용, 작성자 정보, 좋아요 수, 댓글, 조회수, 첨부 파일 포함
     *         - 반환 타입: {@link BoardsResponseDto.UpdateOneDto}
     */
    BoardsResponseDto.UpdateOneDto updateOneGeneralType(
            Long boardId, Members loginMember,
            BoardsRequestDto.UpdateOneDto requestDto,
            List<MultipartFile> files);

    /**
     * 게시글 삭제
     *
     * @param boardId 삭제하려는 게시글의 고유 ID
     * @param loginMember 요청을 수행한 로그인 사용자
     */
    void deleteOne(Long boardId, Members loginMember);

    /**
     * 부서 게시글 생성
     *
     * @param department 게시글이 속한 부서 이름
     * @param loginMember 게시글 생성 요청을 한 로그인 사용자
     * @param requestDto 게시글 생성 요청 데이터 {@link BoardsRequestDto.CreateOneDto}
     * @param files 첨부 파일 리스트
     * @return 생성된 부서 게시글의 정보 {@link BoardsResponseDto.CreateOneDto}
     */
    BoardsResponseDto.CreateOneDto createOneDepartmentType(
            String department, Members loginMember,
            BoardsRequestDto.CreateOneDto requestDto, List<MultipartFile> files);

    /**
     * 부서별 게시글 목록 조회
     *
     * @param departmentName 조회할 부서 이름
     * @param loginMember 로그인 멤버
     * @param pageable 페이징 처리 정보 (페이지 번호, 크기 등)
     * @return 페이징된 부서 게시글 목록 {@link BoardsResponseDto.ReadAllDto}
     */
    Page<BoardsResponseDto.ReadAllDto> readPageDepartmentType(
            String departmentName, Members loginMember, Pageable pageable);

    /**
     * 부서 게시글 상세 조회
     *
     * @param departmentName 게시글이 속한 부서 이름
     * @param boardId 조회하려는 게시글의 고유 식별 ID
     * @param loginMember 로그인 멤버
     * @return 부서 게시글의 세부 정보 {@link BoardsResponseDto.ReadOneDto}
     */
    BoardsResponseDto.ReadOneDto readOneDepartmentType(
            String departmentName, Long boardId, Members loginMember);

    /**
     * 부서 게시글 수정
     *
     * @param department 게시글이 속한 부서 이름
     * @param boardId 수정하려는 게시글의 고유 ID
     * @param loginMember 요청을 수행한 로그인 사용자
     * @param requestDto 게시글 수정 요청 데이터 {@link BoardsRequestDto.UpdateOneDto}
     * @param files 첨부 파일 리스트
     * @return 수정된 부서 게시글의 정보 {@link BoardsResponseDto.UpdateOneDto}
     */
    BoardsResponseDto.UpdateOneDto updateOneForDepartment(
            String department, Long boardId, Members loginMember,
            BoardsRequestDto.UpdateOneDto requestDto,
            List<MultipartFile> files);

    /**
     * 게시글 중요 상태 변경
     *
     * @param boardId 상태를 변경하려는 게시글의 고유 ID
     * @param loginMember 요청을 수행한 로그인 사용자
     */
    void updateOneForMarkAsImportant(Long boardId, Members loginMember);

    /**
     * 게시글 잠금 상태 변경
     *
     * @param boardId 상태를 변경하려는 게시글의 고유 ID
     * @param loginMember 요청을 수행한 로그인 사용자
     */
    void updateOneForMarkAsLocked(Long boardId, Members loginMember);
}
