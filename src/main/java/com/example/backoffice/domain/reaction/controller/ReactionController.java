package com.example.backoffice.domain.reaction.controller;

import com.example.backoffice.domain.reaction.dto.ReactionsRequestDto;
import com.example.backoffice.domain.reaction.dto.ReactionsResponseDto;
import com.example.backoffice.domain.reaction.service.ReactionsService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
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
            @RequestBody ReactionsRequestDto.CreateMemberReactionsRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        ReactionsResponseDto.CreateMemberReactionResponseDto responseDto =
                reactionsService.createMemberReaction(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
}
