package com.example.backoffice.domain.comment.controller;

import com.amazonaws.Response;
import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.service.CommentsService;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PatchMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<CommentsResponseDto.UpdateCommentsResponseDto> updateComment(
            @RequestBody CommentsRequestDto.UpdateCommentsRequestDto requestDto,
            @PathVariable Long boardId, @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.UpdateCommentsResponseDto responseDto =
                commentsService.updateComment(boardId, commentId,
                        requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<Void>> deleteComment(
            @PathVariable Long boardId, @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        commentsService.deleteComment(boardId, commentId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "게시글의 댓글이 삭제되었습니다."
                )
        );
    }
}
