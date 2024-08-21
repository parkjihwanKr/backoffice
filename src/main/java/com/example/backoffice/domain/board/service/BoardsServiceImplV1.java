package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.board.repository.BoardsRepository;
import com.example.backoffice.domain.file.service.FilesServiceV1;
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
        Page<Boards> boardList = boardsRepository.findAll(pageable);
        return BoardsConverter.toReadAllDto(boardList);
    }

    @Override
    @Transactional
    public BoardsResponseDto.ReadOneDto readOne(Long boardId){
        Boards board = findById(boardId);
        incrementViewCount(board);
        return BoardsConverter.toReadOneDto(board);
    }

    @Override
    @Transactional
    public BoardsResponseDto.CreateOneDto createOne(
            Members member, BoardsRequestDto.CreateOneDto requestDto,
            List<MultipartFile> files){
        Boards board = BoardsConverter.toEntity(requestDto, member);
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
