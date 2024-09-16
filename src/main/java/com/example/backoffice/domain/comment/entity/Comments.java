package com.example.backoffice.domain.comment.entity;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.entity.Reactions;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Comments extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Boards board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comments parent;  // 부모 댓글

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> replyList;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reactions> reactionList;

    // entity method
    public void update(String content) {
        this.content = content;
    }

    public void updateParent(Comments comment) {
        this.parent = comment;
    }

    public void addReply(Comments reply) {
        this.replyList.add(reply);
    }

    public void addEmoji(Reactions reaction, String emoji) {
        reactionList.add(reaction);
        if (emoji.equals("LIKE")) {
            this.likeCount++;
        }
    }

    public void deleteEmoji(String emoji) {
        if (emoji.equals("LIKE")) {
            this.likeCount--;
        }
    }
}
