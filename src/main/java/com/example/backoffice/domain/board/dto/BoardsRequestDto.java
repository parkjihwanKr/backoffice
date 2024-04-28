package com.example.backoffice.domain.board.dto;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.image.entity.Images;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBoardRequestDto {
        private String title;
        private String content;

        // 처음 생성되는 Entity의 likeCount = 0 고정
        public Boards toEntity(
                Members member, String title, String content){
            return Boards.builder()
                    .member(member)
                    .title(title)
                    .content(content)
                    .likeCount(0)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBoardRequestDto {
        private String title;
        private String content;
        private MultipartFile file;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateImageBoardRequestDto{
        private MultipartFile file;
    }
}
