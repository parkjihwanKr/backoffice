package com.example.backoffice.domain.comment.controller;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.service.CommentsService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommentsResponseDto.CreateCommentsResponseDto> createComment(
            @RequestBody CommentsRequestDto.CreateCommentsRequestDto requestDto,
            @PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.CreateCommentsResponseDto responseDto =
                commentsService.createComment(requestDto, boardId, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }
}
