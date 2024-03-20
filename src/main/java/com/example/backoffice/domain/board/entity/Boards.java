package com.example.backoffice.domain.board.entity;

import com.example.backoffice.domain.board.dto.BoardsRequestDto;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.image.entity.Images;
import com.example.backoffice.domain.like.entity.Likes;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

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

    private String title;

    private String content;

    private Integer likeCount;

    // feat #1 조회수 ? 구현해보고 싶은데?
    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> commentList;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likeList;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Images> imageList;

    // entity method
    public void update(BoardsRequestDto.UpdateBoardRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
    //
}
