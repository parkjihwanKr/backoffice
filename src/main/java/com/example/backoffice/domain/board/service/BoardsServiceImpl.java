package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.converter.BoardsConverter;
import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.board.repository.BoardsRepository;
import com.example.backoffice.domain.file.service.FilesService;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.redis.RedisProvider;
import com.example.backoffice.global.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService{

    private final BoardsRepository boardsRepository;
    private final FilesService filesService;
    private final RedisProvider redisProvider;
    private final AuthenticationService authenticationService;

    @Override
    @Transactional(readOnly = true)
    public Page<BoardsResponseDto.ReadBoardListResponseDto> readBoard(Pageable pageable){
        Page<Boards> boardList = boardsRepository.findAll(pageable);
        return BoardsResponseDto.ReadBoardListResponseDto.of(boardList);
    }

    @Override
    @Transactional
    public BoardsResponseDto.ReadBoardResponseDto readOne(Long boardId){
        Boards board = findById(boardId);
        incrementViewCount(board);
        return BoardsResponseDto.ReadBoardResponseDto.from(board);
    }

    @Override
    @Transactional
    public BoardsResponseDto.CreateBoardResponseDto createBoard(
            Members member, BoardsRequestDto.CreateBoardRequestDto requestDto,
            List<MultipartFile> files){
        Boards board = BoardsConverter.toEntity(requestDto, member);
        for(int i = 0; i<files.size(); i++) {
            filesService.createFileForBoard(files.get(i), board);
        }
        boardsRepository.save(board);
        return BoardsResponseDto.CreateBoardResponseDto.from(board);
    }

    @Override
    @Transactional
    public BoardsResponseDto.UpdateBoardResponseDto updateBoard(
            Long boardId, Members member,
            BoardsRequestDto.UpdateBoardRequestDto requestDto){
        Boards board = findById(boardId);
        board.update(requestDto);
        filesService.createFileForBoard(requestDto.getFile(), board);
        boardsRepository.save(board);
        return BoardsResponseDto.UpdateBoardResponseDto.from(board);
    }

    @Override
    public BoardsResponseDto.UpdateImageBoardResponseDto updateBoardImage(
            Long boardId, Members member, BoardsRequestDto.UpdateImageBoardRequestDto requestDto){
        Boards board = findById(boardId);
        board.updateFile(requestDto.getFile());
        filesService.createFileForBoard(requestDto.getFile(), board);
        boardsRepository.save(board);
        return BoardsResponseDto.UpdateImageBoardResponseDto.from(board);
    }

    @Transactional(readOnly = true)
    public Boards findById(Long boardId) {
        return boardsRepository.findById(boardId).orElseThrow(
                () -> new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD)
        );
    }

    // 조회수 로직
    private void incrementViewCount(Boards board){
        String currentMemberName = authenticationService.getCurrentMemberName();
        System.out.println("currentMemberName : "+ currentMemberName);
        String key = "boardId : " + board.getId() +
                ", viewMemberName : " + currentMemberName;

        // totalCount를 집계해서 가져 올 것
        // member에 따른 조회 수를 expireDate 없이 redis에서 관리할 것
        // 해당 관리를 스케줄러를 통해 1달이 지나면 가능하게 변경할 것

        Long currentCount = redisProvider.getViewCount(key);
        if (currentCount == null) {
            currentCount = 0L;
        }
        Long viewCount = 0L;
        // 게시글 작성자가 현재 로그인한 사용자와 같은 경우
        if (board.getMember().getMemberName().equals(currentMemberName)) {
            if (currentCount < 1) {
                viewCount = redisProvider.incrementViewCount(key);
                board.incrementViewCount();
            }
        } else {
            // 게시글 작성자가 현재 로그인한 사용자와 다른 경우
            if (currentCount < 3) {
                viewCount = redisProvider.incrementViewCount(key);
                board.incrementViewCount();
            }
        }
    }
}
