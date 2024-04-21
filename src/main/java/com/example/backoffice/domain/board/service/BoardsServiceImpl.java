package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.board.exception.BoardsCustomException;
import com.example.backoffice.domain.board.exception.BoardsExceptionCode;
import com.example.backoffice.domain.board.repository.BoardsRepository;
import com.example.backoffice.domain.image.service.ImagesService;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService{

    private final BoardsRepository boardsRepository;
    private final ImagesService imagesService;
    private final MembersServiceImpl membersService;

    // Paging 처리
    /*@Override
    @Transactional(readOnly = true)
    public Page<BoardsResponseDto.ReadBoardListResponseDto> readBoard(Pageable pageable){
        Page<Boards> boardList = boardsRepository.findBoardsByCreatedAt(pageable);
        return BoardsResponseDto.ReadBoardListResponseDto.of(boardList);
    }*/

    @Override
    @Transactional(readOnly = true)
    public BoardsResponseDto.ReadBoardResponseDto readPost(Long boardId){
        Boards board = findById(boardId);
        return BoardsResponseDto.ReadBoardResponseDto.from(board);
    }

    @Override
    @Transactional
    public BoardsResponseDto.CreateBoardResponseDto createPost(
            Long boardId, Members member,
            BoardsRequestDto.CreateBoardRequestDto requestDto){
        imagesService.uploadFile(requestDto.getFile());
        Boards board = requestDto.toEntity(member);
        boardsRepository.save(board);
        return BoardsResponseDto.CreateBoardResponseDto.from(board);
    }

    @Override
    @Transactional
    public BoardsResponseDto.UpdateBoardResponseDto updatePost(
            Long boardId, Members member,
            BoardsRequestDto.UpdateBoardRequestDto requestDto){
        Boards board = findById(boardId);
        board.update(requestDto);
        imagesService.uploadFile(requestDto.getFile());
        boardsRepository.save(board);
        return BoardsResponseDto.UpdateBoardResponseDto.from(board);
    }

    @Override
    public BoardsResponseDto.UpdateImageBoardResponseDto updatePostImage(
            Long boardId, Members member, BoardsRequestDto.UpdateImageBoardRequestDto requestDto){
        Boards board = findById(boardId);
        board.updateImage(requestDto.getFile());
        imagesService.uploadFile(requestDto.getFile());
        boardsRepository.save(board);
        return BoardsResponseDto.UpdateImageBoardResponseDto.from(board);
    }

    @Transactional(readOnly = true)
    public Boards findById(Long boardId){
        return boardsRepository.findById(boardId).orElseThrow(
                ()-> new BoardsCustomException(BoardsExceptionCode.NOT_FOUND_BOARD)
        );
    }
}
