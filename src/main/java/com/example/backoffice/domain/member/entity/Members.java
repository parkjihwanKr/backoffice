package com.example.backoffice.domain.member.entity;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.reaction.entity.Reactions;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "members")
@AllArgsConstructor
@NoArgsConstructor
public class Members extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 실명
    @Column
    private String name;

    // 게임 접속 아이디
    @Column(unique = true)
    private String memberName;

    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String address;

    @Column(unique = true)
    private String contact;

    private String profileImageUrl;

    private String introduction;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reactions> reactionList;

    private Long loveCount;

    // 직책
    private String position;

    public void updateMemberInfo(
            String name, String email, String address,
            String contact, String introduction, String bCrytPassword){
        this.name = name;
        this.password = bCrytPassword;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.introduction = introduction;
    }

    public void updateProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void addEmoji(Reactions reaction){
        this.reactionList.add(reaction);
        this.loveCount++;
    }

    public void updatePosition(String position){
        this.position = position;
    }
    public void deleteEmoji(){
        this.loveCount--;
    }
}
