package com.example.backoffice.domain.comment.controller;

import com.example.backoffice.domain.comment.dto.CommentsRequestDto;
import com.example.backoffice.domain.comment.dto.CommentsResponseDto;
import com.example.backoffice.domain.comment.service.CommentsServiceV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Comments API", description = "게시글의 댓글 또는 답글 API")
public class CommentsController {

    private final CommentsServiceV1 commentsService;

    @PostMapping("/boards/{boardId}/comments")
    @Operation(summary = "게시글 하나의 댓글 생성",
            description = "로그인한 사용자는 게시글 하나의 댓글을 작성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 하나의 댓글 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.CreateCommentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommentsResponseDto.CreateCommentDto> createComment(
            @RequestBody CommentsRequestDto.CreateCommentDto requestDto,
            @PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommentsResponseDto.CreateCommentDto responseDto =
                commentsService.createComment(requestDto, boardId, memberDetails.getMembers());
        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/boards/{boardId}/comments/{commentId}")
    @Operation(summary = "댓글 한 개 수정",
            description = "자신이 작성한 댓글을 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.UpdateCommentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "댓글 한 개 삭제",
            description = "자신이 작성한 댓글을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 한 개 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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

    @PostMapping("/boards/{boardId}/comments/{commentId}/replies")
    @Operation(summary = "답글 한 개 생성",
            description = "로그인한 사용자는 댓글 한 개에 답글을 작성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "답글 한 개 삭제 수정",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.CreateReplyDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "답글 한 개 수정",
            description = "자신이 작성한 답글을 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "답글 한 개 삭제 수정",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.UpdateReplyDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "답글 한 개 삭제",
            description = "자신이 작성한 답글을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "답글 한 개 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
