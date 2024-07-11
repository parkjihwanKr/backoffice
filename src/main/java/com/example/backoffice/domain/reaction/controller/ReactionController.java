package com.example.backoffice.domain.reaction.controller;

import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.service.ReactionsService;
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
public class ReactionController {

    private final ReactionsService reactionsService;

    @PostMapping("/member/{memberId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateMemberReactionResponseDto> createMemberReaction(
            @PathVariable Long memberId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateMemberReactionResponseDto responseDto =
                reactionsService.createMemberReaction(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/member/{memberId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteMemberReaction(
            @PathVariable Long memberId, @PathVariable Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteMemberReaction(
                memberId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "멤버 리액션 취소 성공"
                )
        );
    }

    @PostMapping("/boards/{boardId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateBoardReactionResponseDto> createBoardReaction(
            @PathVariable Long boardId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateBoardReactionResponseDto responseDto =
                reactionsService.createBoardReaction(
                        boardId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/boards/{boardId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteBoardReaction(
            @PathVariable Long boardId, @PathVariable Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteBoardReaction(
                boardId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "게시글 리액션 삭제 성공"
                )
        );
    }

    @PostMapping("/boards/{boardId}/comments/{commentId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateCommentReactionResponseDto> createCommentReaction(
            @PathVariable Long boardId, @PathVariable Long commentId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateCommentReactionResponseDto responseDto =
                reactionsService.createCommentReaction(
                        boardId, commentId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/comments/{commentId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteCommentReaction(
            @PathVariable Long commentId, @PathVariable Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteCommentReaction(
                commentId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "댓글 리액션 삭제 성공"
                )
        );
    }

    @PostMapping("/comments/{commentId}/replies/{replyId}/reactions")
    public ResponseEntity<ReactionsResponseDto.CreateReplyReactionResponseDto> createReplyReaction(
            @PathVariable Long commentId, @PathVariable Long replyId,
            @RequestBody ReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ReactionsResponseDto.CreateReplyReactionResponseDto responseDto =
                reactionsService.createReplyReaction(
                        commentId, replyId, memberDetails.getMembers(), requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/replies/{replyId}/reactions/{reactionId}")
    public ResponseEntity<CommonResponse<Void>> deleteReplyReaction(
            @PathVariable Long replyId, @PathVariable Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteReplyReaction(
                replyId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(
                        HttpStatus.OK,
                        "대댓글 리액션 취소 성공"
                )
        );
    }
}
