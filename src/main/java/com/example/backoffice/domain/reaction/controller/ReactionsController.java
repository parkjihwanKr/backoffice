package com.example.backoffice.domain.reaction.controller;

import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.service.ReactionsServiceV1;
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
public class ReactionsController {

    private final ReactionsServiceV1 reactionsService;

    @PostMapping("/member/{memberId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateOneForMemberDto> createOneForMember(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateOneForMemberDto responseDto
                = reactionsService.createOneForMember(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/member/{memberId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteOneForMember(
            @PathVariable(name = "memberId") Long memberId,
            @PathVariable(name = "reactionId") Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteOneForMember(
                memberId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(HttpStatus.OK, "멤버 리액션 취소 성공"));
    }

    @PostMapping("/boards/{boardId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateOneForBoardDto> createOneForBoard(
            @PathVariable(name = "boardId") Long boardId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateOneForBoardDto responseDto =
                reactionsService.createOneForBoard(
                        boardId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/boards/{boardId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteOneForBoard(
            @PathVariable Long boardId, @PathVariable Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteOneForBoard(
                boardId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(HttpStatus.OK, "게시글 리액션 삭제 성공"));
    }

    @PostMapping("/boards/{boardId}/comments/{commentId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateOneForCommentDto> createOneForComment(
            @PathVariable(name = "boardId") Long boardId,
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateOneForCommentDto responseDto
                = reactionsService.createOneForComment(
                        boardId, commentId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/comments/{commentId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteOneForComment(
            @PathVariable(name = "commentId") Long commentId,
            @PathVariable(name = "reactionId") Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteOneForComment(
                commentId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(HttpStatus.OK, "댓글 리액션 삭제 성공"));
    }

    @PostMapping("/comments/{commentId}/replies/{replyId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateOneForReplyDto> createOneForReply(
            @PathVariable(name = "commentId") Long commentId,
            @PathVariable(name = "replyId") Long replyId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateOneForReplyDto responseDto
                = reactionsService.createOneForReply(
                        commentId, replyId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/replies/{replyId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteOneForReply(
            @PathVariable(name = "replyId") Long replyId,
            @PathVariable(name = "reactionId") Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteOneForReply(
                replyId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(HttpStatus.OK, "대댓글 리액션 취소 성공"));
    }
}
