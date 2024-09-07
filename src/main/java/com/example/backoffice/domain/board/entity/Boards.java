package com.example.backoffice.domain.board.entity;

import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.reaction.entity.Reactions;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Boards extends CommonEntity {

    // field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String content;

    @Column
    private Long viewCount;

    @Column
    private Long likeCount;

    @Column
    private Long unLikeCount;

    private Boolean isImportant;

    private Boolean isLocked;

    @Enumerated(EnumType.STRING)
    private BoardCategories categories;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Enumerated(EnumType.STRING)
    private MemberDepartment department;

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

    public void update(
            String title, String content, Boolean isImportant,
            Boolean isLocked, MemberDepartment department,
            BoardCategories categories){
        this.title = title;
        this.content = content;
        this.isImportant = isImportant;
        this.isLocked = isLocked;
        this.department = department;
        this.categories = categories;
    }

    public void updateIsImportant(Boolean isImportant){
        this.isImportant = isImportant;
    }

    public void updateIsLocked(Boolean isLocked){
        this.isLocked = isLocked;
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
