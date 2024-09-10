package com.example.backoffice.domain.board.entity;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.reaction.entity.Reactions;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@Table(name = "boards")
@NoArgsConstructor
@AllArgsConstructor
public class Boards extends CommonEntity {

    // field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Long viewCount;

    @Column
    private Long likeCount;

    @Column
    private Long unLikeCount;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reactions> reactionList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Files> fileList = new ArrayList<>();

    // entity method
    public void incrementViewCount(){
        this.viewCount++;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addEmoji(Reactions reaction, String emoji){
        reactionList.add(reaction);
        if(emoji.equals("LIKE")){
            this.likeCount++;
        }else if(emoji.equals("UNLIKE")){
            this.unLikeCount++;
        }
    }

    public void deleteEmoji(String emoji){
        if(emoji.equals("LIKE")){
            this.likeCount--;
        }
        if(emoji.equals("UNLIKE")){
            this.unLikeCount--;
        }
    }
}
