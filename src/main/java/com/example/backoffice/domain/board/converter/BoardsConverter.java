package com.example.backoffice.domain.board.converter;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public class BoardsConverter {

    public static Boards toEntity(
            BoardsRequestDto.CreateBoardRequestDto requestDto, Members member){
        return Boards.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .likeCount(0L)
                .viewCount(0L)
                .build();
    }

    public static Page<BoardsResponseDto.ReadBoardListResponseDto> toReadDto(Page<Boards> boardPage){
        return boardPage.map(board -> {
            return BoardsResponseDto.ReadBoardListResponseDto.builder()
                    .title(board.getTitle())
                    .writer(board.getMember().getMemberName())
                    .likeCount(board.getLikeCount())
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .build();
        });
    }

    public static BoardsResponseDto.ReadBoardResponseDto toReadOneDto(Boards board){

        List<String> fileUrls = board.getFileList().stream()
                .map(Files::getUrl)
                .collect(Collectors.toList());

        // Comments의 Comments까지 구현해야하기에 아직
            /*List<Comments> commentContents = board.getCommentList().stream()
                    .map(Comments::getContent)
                    .collect(Collectors.toList());*/

        return BoardsResponseDto.ReadBoardResponseDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getMemberName())
                .likeCount(board.getLikeList().size())
                .viewCount(board.getViewCount())
                .commentList(board.getCommentList())
                .fileList(fileUrls)
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }

    public static BoardsResponseDto.CreateBoardResponseDto toCreateDto(Boards board){
        return BoardsResponseDto.CreateBoardResponseDto.builder()
                .writer(board.getMember().getMemberName())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardsResponseDto.UpdateBoardResponseDto toUpdateDto(Boards board){
        return BoardsResponseDto.UpdateBoardResponseDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getMemberName())
                .commentList(board.getCommentList())
                .likeCount(0L)
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardsResponseDto.UpdateImageBoardResponseDto toUpdateImageDto(Boards board){
        return BoardsResponseDto.UpdateImageBoardResponseDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getMemberName())
                .commentList(board.getCommentList())
                .likeCount(0L)
                .createdAt(board.getCreatedAt())
                .build();
    }
}
