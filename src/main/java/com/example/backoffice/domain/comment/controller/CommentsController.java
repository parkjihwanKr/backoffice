package com.example.backoffice.domain.comment.controller;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.service.CommentsServiceV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentsController {

    private final CommentsServiceV1 commentsService;

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<CommentsResponseDto.CreateCommentDto> createComment(
            @RequestBody CommentsRequestDto.CreateCommentDto requestDto,
            @PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.CreateCommentDto responseDto =
                commentsService.createComment(requestDto, boardId, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<CommentsResponseDto.UpdateCommentDto> updateComment(
            @RequestBody CommentsRequestDto.UpdateCommentDto requestDto,
            @PathVariable Long boardId, @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.UpdateCommentDto responseDto =
                commentsService.updateComment(
                        boardId, commentId, requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
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

    @PostMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<CommentsResponseDto.CreateReplyDto> createReply(
            @PathVariable Long boardId, @PathVariable Long commentId,
            @RequestBody CommentsRequestDto.CreateReplyDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.CreateReplyDto responseDto =
                commentsService.createReply(
                        boardId, commentId, requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<CommentsResponseDto.UpdateReplyDto> updateReply(
            @PathVariable Long commentId, @PathVariable Long replyId,
            @RequestBody CommentsRequestDto.UpdateReplyDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.UpdateReplyDto responseDto =
                commentsService.updateReply(commentId, replyId,
                        requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<CommonResponse<Void>> deleteReply(
            @PathVariable Long commentId, @PathVariable Long replyId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        commentsService.deleteReply(
                commentId, replyId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "대댓글 삭제 성공"
                )
        );
    }
}
