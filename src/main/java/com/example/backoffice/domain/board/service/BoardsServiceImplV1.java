package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.board.repository.BoardsRepository;
import com.example.backoffice.domain.file.service.FilesServiceV1;
import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.redis.ViewCountRedisProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsServiceImplV1 implements BoardsServiceV1 {

    private final BoardsRepository boardsRepository;
    private final FilesServiceV1 filesService;
    private final ViewCountRedisProvider viewCountRedisProvider;

    @Override
    @Transactional(readOnly = true)
    public Page<BoardsResponseDto.ReadAllDto> readAll(Pageable pageable){
        Page<Boards> boardList
                = boardsRepository.findAllByBoardType(pageable, BoardType.GENERAL);
        return BoardsConverter.toReadAllDto(boardList);
    }

    @Override
    @Transactional
    public BoardsResponseDto.ReadOneDto readOne(Long boardId){
        Boards board = findById(boardId);
        if(!board.getBoardType().equals(BoardType.GENERAL)){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_GENERAL_BOARD);
        }
        incrementViewCount(board);
        return BoardsConverter.toReadOneDto(board);
    }

    @Override
    @Transactional
    public BoardsResponseDto.CreateOneDto createOne(
            Members loginMember, BoardsRequestDto.CreateOneDto requestDto,
            List<MultipartFile> files){
        // department 입력란에 null 일 때?

        // 만들 자격 추가 : 전체 게시판을 만들 수 있는 인원은 권한이 admin이거나 main_admin만 가능
        if(!(loginMember.getRole().equals(MemberRole.MAIN_ADMIN)
                || loginMember.getRole().equals(MemberRole.ADMIN))){
            throw new BoardsCustomException(BoardsExceptionCode.UNAUTHORIZED_DEPARTMENT_BOARD_CREATION);
        }
        Boards board = BoardsConverter.toEntity(requestDto, loginMember, BoardType.GENERAL);
        List<String> fileUrlList = new ArrayList<>();
        boardsRepository.save(board);
        for (MultipartFile file : files) {
            String fileName = filesService.createOneForBoard(file, board);
            fileUrlList.add(fileName);
        }
        return BoardsConverter.toCreateOneDto(board, fileUrlList);
    }

    @Override
    @Transactional
    public BoardsResponseDto.UpdateOneDto updateOne(
            Long boardId, Members member,
            BoardsRequestDto.UpdateOneDto requestDto,
            List<MultipartFile> files){
        Boards board = findById(boardId);
        board.update(requestDto.getTitle(), requestDto.getContent());
        // 삭제 전 urlList, 삭제 후 urlList
        List<String> beforeFileUrlList = new ArrayList<>();
        List<String> afterFileUrlList = new ArrayList<>();

        for(int i = 0; i<board.getFileList().size(); i++){
            beforeFileUrlList.add(board.getFileList().get(i).getUrl());
        }
        board.getFileList().clear();
        filesService.delete(board.getId(), beforeFileUrlList);
        for (MultipartFile file : files) {
            // s3는 수정 관련 메서드가 없기에 제거 후, 재생성하는 방향
            String fileUrl = filesService.createOneForBoard(file, board);
            afterFileUrlList.add(fileUrl);
        }
        return BoardsConverter.toUpdateOneDto(board, afterFileUrlList);
    }

    @Override
    @Transactional
    public void deleteOne(Long boardId, Members member){
        Boards board = findById(boardId);
        if(!member.getId().equals(board.getMember().getId())){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_MATCHED_MEMBER);
        }
        boardsRepository.deleteById(boardId);
    }

    @Override
    @Transactional
    public BoardsResponseDto.CreateOneDto createOneForDepartment(
            Members loginMember, BoardsRequestDto.CreateOneDto requestDto,
            List<MultipartFile> files){
        // 1. 해당 멤버가 부서 게시판을 만들 자격이 있는지?
        String department = requestDto.getDepartment();
        MemberDepartment memberDepartment
                = MembersConverter.toDepartment(department);
        // 1-1. 만드려는 부서 게시판의 멤버 부서가 같지 않을 때
        if(!loginMember.getDepartment().equals(memberDepartment)) {
            // 1-2. 만드려는 부서 게시판과 멤버 부서가 같지 않더라도 메인 어드민은 가능
            if (!loginMember.getRole().equals(MemberRole.MAIN_ADMIN)) {
                throw new BoardsCustomException(
                        BoardsExceptionCode.UNAUTHORIZED_DEPARTMENT_BOARD_CREATION);
            }
        }

        Boards departmentBoard = BoardsConverter.toEntity(
                requestDto, loginMember, BoardType.DEPARTMENT);

        List<String> fileUrlList = new ArrayList<>();
        boardsRepository.save(departmentBoard);
        for (MultipartFile file : files) {
            String fileName = filesService.createOneForBoard(file, departmentBoard);
            fileUrlList.add(fileName);
        }

        return BoardsConverter.toCreateOneDto(departmentBoard, fileUrlList);
    }

    // 모든 멤버가 접근은 가능하되, 자기가 포함되어진 부서가 아닌 멤버에게는 읽기만 허용
    @Override
    @Transactional(readOnly = true)
    public Page<BoardsResponseDto.ReadAllDto> readAllForDepartment(Pageable pageable){
        Page<Boards> boardList
                = boardsRepository.findAllByBoardType(pageable, BoardType.DEPARTMENT);
        return BoardsConverter.toReadAllDto(boardList);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardsResponseDto.ReadOneDto readOneForDepartment(Long boardId){
        Boards departmentBoard = findById(boardId);
        if(!departmentBoard.getBoardType().equals(BoardType.DEPARTMENT)){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_DEPARTMENT_BOARD);
        }
        incrementViewCount(departmentBoard);
        return BoardsConverter.toReadOneDto(departmentBoard);
    }

    @Transactional(readOnly = true)
    public Boards findById(Long boardId) {
        return boardsRepository.findById(boardId).orElseThrow(
                () -> new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD)
        );
    }

    // 조회수 로직
    void incrementViewCount(Boards board){
        String currentMemberName
                = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = "boardId : " + board.getId() +
                ", viewMemberName : " + currentMemberName;

        // totalCount를 집계해서 가져 올 것
        // member에 따른 조회 수를 expireDate 없이 redis에서 관리할 것
        // 해당 관리를 스케줄러를 통해 1달이 지나면 가능하게 변경할 것

        Long currentCount = viewCountRedisProvider.getViewCount(key);
        if (currentCount == null) {
            currentCount = 0L;
        }
        Long viewCount;
        // 게시글 작성자가 현재 로그인한 사용자와 같은 경우
        if (board.getMember().getMemberName().equals(currentMemberName)) {
            if (currentCount < 1) {
                viewCount = viewCountRedisProvider.incrementViewCount(key);
                board.incrementViewCount();
            }
        } else {
            // 게시글 작성자가 현재 로그인한 사용자와 다른 경우
            if (currentCount < 3) {
                viewCount = viewCountRedisProvider.incrementViewCount(key);
                board.incrementViewCount();
            }
        }
    }
}
