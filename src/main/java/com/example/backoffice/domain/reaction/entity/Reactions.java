package com.example.backoffice.domain.reaction.entity;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.member.entity.Members;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reactions {

    // field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Emoji emoji;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Members member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Boards board;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comments comment;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Comments reply;
    // entity method

}
