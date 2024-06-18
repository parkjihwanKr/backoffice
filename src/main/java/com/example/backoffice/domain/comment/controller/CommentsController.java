package com.example.backoffice.domain.comment.controller;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.dto.RepliesRequestDto;
import com.example.backoffice.domain.comment.dto.RepliesResponseDto;
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
    public ResponseEntity<CommentsResponseDto.CreateOneDto> createOneComment(
            @RequestBody CommentsRequestDto.CreateOneDto requestDto,
            @PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.CreateOneDto responseDto =
                commentsService.createOneComment(requestDto, boardId, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<CommentsResponseDto.UpdateOneDto> updateOneComment(
            @RequestBody CommentsRequestDto.UpdateOneDto requestDto,
            @PathVariable Long boardId, @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.UpdateOneDto responseDto =
                commentsService.updateOneComment(
                        boardId, commentId, requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<Void>> deleteOneComment(
            @PathVariable Long boardId, @PathVariable Long commentId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        commentsService.deleteOneComment(boardId, commentId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "게시글의 댓글이 삭제되었습니다."
                )
        );
    }

    @PostMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<RepliesResponseDto.CreateOneDto> createOneReply(
            @PathVariable Long boardId, @PathVariable Long commentId,
            @RequestBody RepliesRequestDto.CreateOneDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        RepliesResponseDto.CreateOneDto responseDto =
                commentsService.createOneReply(
                        boardId, commentId, requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<RepliesResponseDto.UpdateOneDto> updateOneReply(
            @PathVariable Long commentId, @PathVariable Long replyId,
            @RequestBody RepliesRequestDto.UpdateOneDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        RepliesResponseDto.UpdateOneDto responseDto =
                commentsService.updateOneReply(commentId, replyId,
                        requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<CommonResponse<Void>> deleteOneReply(
            @PathVariable Long commentId, @PathVariable Long replyId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        commentsService.deleteOneReply(
                commentId, replyId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "대댓글 삭제 성공"
                )
        );
    }
}
