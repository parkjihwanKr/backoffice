package com.example.backoffice.domain.reaction.controller;

import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.service.ReactionsServiceV1;
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
@Tag(name = "Reactions API", description = "리액션 API")
public class ReactionsController {

    private final ReactionsServiceV1 reactionsService;

    @PostMapping("/member/{memberId}/reactions")
    @Operation(summary = "멤버 리액션 생성",
            description = "로그인 사용자는 특정 멤버에게 '좋아요' 이모지를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 '좋아요' 이모지 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReactionsResponseDto.CreateOneForMemberDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "멤버 리액션 삭제",
            description = "로그인 사용자는 특정 멤버에게 '좋아요' 이모지를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 '좋아요' 이모지 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "게시글 리액션 생성",
            description = "로그인 사용자는 특정 게사글에 '좋아요' 이모지를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 '좋아요' 이모지 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReactionsResponseDto.CreateOneForMemberDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "게시글 리액션 삭제",
            description = "로그인 사용자는 특정 멤버에게 '좋아요' 이모지를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 '좋아요' 이모지 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<Void>> deleteOneForBoard(
            @PathVariable Long boardId, @PathVariable Long reactionId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        reactionsService.deleteOneForBoard(
                boardId, reactionId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponse<>(HttpStatus.OK, "게시글 리액션 삭제 성공"));
    }

    @PostMapping("/boards/{boardId}/comments/{commentId}/reactions")
    @Operation(summary = "게시글 댓글 리액션 생성",
            description = "로그인 사용자는 특정 게사글의 댓글에 '좋아요' 이모지를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 댓글 '좋아요' 이모지 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    ReactionsResponseDto.CreateOneForCommentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "게시글 댓글 리액션 삭제",
            description = "로그인 사용자는 특정 게시글 댓글에 '좋아요' 이모지를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 댓글 '좋아요' 이모지 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "대댓글 리액션 생성",
            description = "로그인 사용자는 특정 댓글의 대댓글에 '좋아요' 이모지를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 '좋아요' 이모지 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    ReactionsResponseDto.CreateOneForReplyDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "댓글의 대댓글 리액션 삭제",
            description = "로그인 사용자는 특정 댓글의 대댓글에 '좋아요' 이모지를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 '좋아요' 이모지 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
