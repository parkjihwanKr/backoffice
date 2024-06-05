package com.example.backoffice.domain.notification.entity;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationData {

    // 해당 부분 리팩토링 예정
    private Members toMember;
    private Members fromMember;
    private Boards board;
    private Comments comment;
    private Comments reply;
    private Events event;
}
