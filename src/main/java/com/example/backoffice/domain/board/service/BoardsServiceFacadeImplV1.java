package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.BoardCategories;
import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.file.service.FilesServiceV1;
import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.service.ReactionsServiceV1;
import com.example.backoffice.global.redis.ViewCountRedisProvider;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardsServiceFacadeImplV1 implements BoardsServiceFacadeV1{

    private final BoardsServiceV1 boardsService;
    private final ReactionsServiceV1 reactionsService;
    private final FilesServiceV1 filesService;
    private final ViewCountRedisProvider viewCountRedisProvider;

    @Override
    @Transactional(readOnly = true)
    public Page<BoardsResponseDto.ReadAllDto> readAll(Pageable pageable) {
        // 1. 중요한 게시글(isImportant == true)을 ModifiedAt 기준으로 내림차순 정렬하여 가져옵니다.
        List<Boards> importantBoardsByModifiedAt
                = boardsService.findByIsImportantTrueOrderByModifiedAtDesc();

        // 2. 상단에 고정할 중요한 게시글 3개를 추출합니다.
        List<Boards> topImportantBoards = importantBoardsByModifiedAt.stream()
                .limit(3)
                .toList();

        // 3. 나머지 중요한 게시글은 CreatedAt 기준으로 정렬합니다.
        List<Boards> remainingImportantBoards = importantBoardsByModifiedAt.stream()
                .skip(3)
                .sorted(Comparator.comparing(Boards::getCreatedAt))
                .toList();

        // 4. 중요한 게시글 외의 일반 게시글(isImportant == false)을 CreatedAt 기준으로 가져옵니다.
        Page<Boards> otherBoardPage = boardsService.findByIsImportantFalseOrderByCreatedAtDesc(pageable);

        // 5. 중요한 게시글과 일반 게시글을 합칩니다.
        List<Boards> combinedBoards = new ArrayList<>(topImportantBoards);
        combinedBoards.addAll(remainingImportantBoards);  // 남은 중요한 게시글 추가
        combinedBoards.addAll(otherBoardPage.getContent());  // 일반 게시글 추가

        // 6. 댓글 수를 각 게시글에 추가
        List<BoardsResponseDto.ReadAllDto> boardDtoList = combinedBoards.stream()
                .map(board -> {
                    Long commentCount = boardsService.getCommentListSize(board);  // 댓글 수 계산
                    return BoardsConverter.toReadAllDto(board, commentCount);
                })
                .collect(Collectors.toList());

        // 7. 전체 게시글의 수를 계산합니다 (중요 게시글 + 나머지 게시글)
        long totalBoardCount = importantBoardsByModifiedAt.size() + otherBoardPage.getTotalElements();

        // 8. combinedBoards 리스트를 Page로 변환하여 반환합니다.
        return new PageImpl<>(boardDtoList, pageable, totalBoardCount);
    }

    @Override
    @Transactional
    public BoardsResponseDto.ReadOneDto readOne(Long boardId){
        Boards board = boardsService.findById(boardId);
        if(!board.getBoardType().equals(BoardType.GENERAL)){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_GENERAL_BOARD);
        }
        List<ReactionsResponseDto.ReadOneForBoardDto> reactionResponseDtoList
                = reactionsService.readAllForBoard(boardId);

        incrementViewCount(board);
        return BoardsConverter.toReadOneDto(
                board, reactionResponseDtoList);
    }

    @Override
    @Transactional
    public BoardsResponseDto.CreateOneDto createOne(
            Members loginMember, BoardsRequestDto.CreateOneDto requestDto,
            List<MultipartFile> files){

        // 만들 자격 추가 : 전체 게시판을 만들 수 있는 인원은 권한이 admin이거나 main_admin만 가능
        if(!(loginMember.getRole().equals(MemberRole.MAIN_ADMIN)
                || loginMember.getRole().equals(MemberRole.ADMIN))){
            throw new BoardsCustomException(BoardsExceptionCode.UNAUTHORIZED_DEPARTMENT_BOARD_CREATION);
        }
        BoardCategories categories
                = BoardsConverter.toCategories(requestDto.getCategory());
        Boards board = BoardsConverter.toEntity(requestDto, loginMember, categories);
        return saveBoardWithFiles(files, board);
    }

    @Override
    @Transactional
    public BoardsResponseDto.UpdateOneDto updateOne(
            Long boardId, Members loginMember,
            BoardsRequestDto.UpdateOneDto requestDto,
            List<MultipartFile> files){
        Boards board = boardsService.findById(boardId);

        if(!board.getBoardType().equals(BoardType.GENERAL)){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_GENERAL_BOARD);
        }

        // 해당 멤버가 게시판의 주인인지?
        isMatchedBoardOwner(loginMember.getId(), board.getMember().getId());

        return updateBoardWithFiles(loginMember.getName(), board, requestDto, files);
    }

    @Override
    @Transactional
    public void deleteOne(Long boardId, Members loginMember){
        Boards board = boardsService.findById(boardId);
        isMatchedBoardOwner(loginMember.getId(),board.getMember().getId());
        boardsService.deleteById(boardId);
    }

    @Override
    @Transactional
    public BoardsResponseDto.CreateOneDto createOneForDepartment(
            String department, Members loginMember,
            BoardsRequestDto.CreateOneDto requestDto, List<MultipartFile> files){
        // 1. 해당 멤버가 부서 게시판을 만들 자격이 있는지?
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

        Boards departmentBoard = BoardsConverter.toEntityForDepartment(
                requestDto, loginMember, memberDepartment);
        return saveBoardWithFiles(files, departmentBoard);
    }

    // 모든 멤버가 접근은 가능하되, 자기가 포함되어진 부서가 아닌 멤버에게는 읽기만 허용
    @Override
    @Transactional(readOnly = true)
    public Page<BoardsResponseDto.ReadAllDto> readAllForDepartment(
            String departmentName, Pageable pageable){
        // 1. 부서에 해당하는 게시글 모두 조회
        MemberDepartment department = MembersConverter.toDepartment(departmentName);
        Page<Boards> boardList = boardsService.findAllByDepartment(pageable, department);

        Members loginMember = getLoginMember();

        // 2. 접근 가능한 게시글 필터링
        List<Boards> accessedBoards = boardList.stream()
                .filter(board -> !board.getIsLocked() || board.getDepartment().equals(loginMember.getDepartment()))
                .toList();

        // 3. 각 게시글의 댓글 수를 계산하여 DTO로 변환
        List<BoardsResponseDto.ReadAllDto> boardDtoList = accessedBoards.stream()
                .map(board -> {
                    Long commentCount = boardsService.getCommentListSize(board);  // 댓글 수 계산
                    return BoardsConverter.toReadAllDto(board, commentCount);
                })
                .toList();

        // 4. 필터링된 리스트를 Page로 변환
        return new PageImpl<>(boardDtoList, pageable, accessedBoards.size());
    }


    @Override
    @Transactional(readOnly = true)
    public BoardsResponseDto.ReadOneDto readOneForDepartment(
            String departmentName, Long boardId){
        MemberDepartment department = MembersConverter.toDepartment(departmentName);

        Boards departmentBoard = boardsService.findByIdAndDepartment(boardId, department);

        if(departmentBoard.getIsLocked()
                && !getLoginMember().getDepartment().equals(
                departmentBoard.getDepartment())){
            throw new BoardsCustomException(BoardsExceptionCode.UNAUTHORIZED_ACCESS);
        }

        // 잠시 null로
        incrementViewCount(departmentBoard);
        return BoardsConverter.toReadOneDto(
                departmentBoard, null);
    }

    @Override
    @Transactional
    public BoardsResponseDto.UpdateOneDto updateOneForDepartment(
            String department, Long boardId, Members loginMember,
            BoardsRequestDto.UpdateOneDto requestDto,
            List<MultipartFile> files){
        // 1. 게시글 존재 유무
        Boards departmentBoard = boardsService.findById(boardId);

        // 2. 게시글의 타입 확인
        if(!departmentBoard.getBoardType().equals(BoardType.DEPARTMENT)){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_DEPARTMENT_BOARD);
        }

        // 3. 게시글의 소유자 확인
        isMatchedBoardOwner(loginMember.getId(), departmentBoard.getMember().getId());

        return updateBoardWithFiles(loginMember.getName(), departmentBoard, requestDto, files);
    }

    @Override
    @Transactional
    public void deleteOneForDepartment(
            String department, Long boardId, Members loginMember){

    }

    // RequestPart files required = false로 인하여 files가 null일 수 있는 경우의 수 발생
    @Transactional
    public BoardsResponseDto.CreateOneDto saveBoardWithFiles(List<MultipartFile> files, Boards board) {
        boardsService.save(board);

        List<String> fileUrlList = new ArrayList<>();
        if(files != null){
            for (MultipartFile file : files) {
                String fileName = filesService.createOneForBoard(file, board);
                fileUrlList.add(fileName);
            }
        }

        return BoardsConverter.toCreateOneDto(board, fileUrlList);
    }

    @Override
    @Transactional
    public void updateOneForMarkAsImportant(Long boardId, Members loginMember){
        Boards board = boardsService.findById(boardId);
        isMatchedBoardOwner(loginMember.getId(), board.getMember().getId());

        board.updateIsImportant(!board.getIsImportant());
    }

    @Override
    @Transactional
    public void updateOneForMarkAsLocked(Long boardId, Members loginMember){
        Boards board = boardsService.findById(boardId);
        if(!board.getBoardType().equals(BoardType.DEPARTMENT)){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_DEPARTMENT_BOARD);
        }
        isMatchedBoardOwner(loginMember.getId(), board.getMember().getId());

        board.updateIsLocked(!board.getIsLocked());
    }

    private void isMatchedBoardOwner(Long memberId, Long boardOwnerId){
        if(!boardOwnerId.equals(memberId)){
            throw new BoardsCustomException(BoardsExceptionCode.NOT_MATCHED_BOARD_OWNER);
        }
    }

    private Members getLoginMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("사용자가 인증되지 않았습니다.");
        }
        MemberDetailsImpl memberDetails = (MemberDetailsImpl) authentication.getPrincipal();
        return memberDetails.getMembers();
    }

    @Transactional(rollbackFor = Exception.class)
    public BoardsResponseDto.UpdateOneDto updateBoardWithFiles(
            String writerName, Boards board,
            BoardsRequestDto.UpdateOneDto requestDto,
            List<MultipartFile> files){
        // Department board, General board 구분
        MemberDepartment department
                = MembersConverter.toDepartment(requestDto.getDepartment());
        if(requestDto.getCategory() == null){
            // department
            board.update(requestDto.getTitle(), requestDto.getContent(),
                    requestDto.getIsImportant(), requestDto.getIsLocked(),
                    department, null);
        }else{
            // general
            BoardCategories categories
                    = BoardsConverter.toCategories(requestDto.getCategory());
            board.update(requestDto.getTitle(), requestDto.getContent(),
                    requestDto.getIsImportant(), false,
                    department, categories);
        }
        // 삭제 전 urlList, 삭제 후 urlList
        List<String> beforeFileUrlList = new ArrayList<>();
        List<String> afterFileUrlList = new ArrayList<>();

        for(int i = 0; i<board.getFileList().size(); i++){
            beforeFileUrlList.add(board.getFileList().get(i).getUrl());
        }
        board.getFileList().clear();
        filesService.delete(board.getId(), beforeFileUrlList);

        if(files != null){
            for (MultipartFile file : files) {
                // s3는 수정 관련 메서드가 없기에 제거 후, 재생성하는 방향
                String fileUrl = filesService.createOneForBoard(file, board);
                afterFileUrlList.add(fileUrl);
            }
        }

        return BoardsConverter.toUpdateOneDto(writerName, board, afterFileUrlList);
    }

    // 조회수 로직
    private void incrementViewCount(Boards board){
        String currentMemberName = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("anonymousUser");

        String key = "boardId : " + board.getId() +
                ", viewMemberName : " + currentMemberName;

        // totalCount를 집계해서 가져 올 것
        // member에 따른 조회 수를 expireDate 없이 redis에서 관리할 것
        // 해당 관리를 스케줄러를 통해 1달이 지나면 가능하게 변경할 것

        Long currentCount = viewCountRedisProvider.getViewCount(key);
        if (currentCount == null) {
            currentCount = 0L;
        }

        // Long viewCount;
        // 게시글 작성자가 현재 로그인한 사용자와 같은 경우
        if (board.getMember().getMemberName().equals(currentMemberName)) {
            if (currentCount < 1) {
                viewCountRedisProvider.incrementViewCount(key);
                board.incrementViewCount();
            }
        } else {
            // 게시글 작성자가 현재 로그인한 사용자와 다른 경우
            if (currentCount < 3) {
                viewCountRedisProvider.incrementViewCount(key);
                board.incrementViewCount();
            }
        }
    }
}
