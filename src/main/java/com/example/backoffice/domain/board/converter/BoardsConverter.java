package com.example.backoffice.domain.board.converter;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

public class BoardsConverter {

    public static Boards toEntity(
            BoardsRequestDto.CreateBoardRequestDto requestDto,
            MultipartFile file, Members member){
        return Boards.builder()
                .member(member)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageList(
                    null
                ).build();
    }
}
