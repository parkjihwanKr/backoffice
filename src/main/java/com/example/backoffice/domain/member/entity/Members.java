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

    public void updateMemberInfo(MembersRequestDto.UpdateMemberRequestDto requestDto, String bCrytPassword){
        this.name = requestDto.getName();
        this.password = bCrytPassword;
        this.email = requestDto.getEmail();
        this.address = requestDto.getAddress();
        this.contact = requestDto.getContact();
        this.introduction = requestDto.getIntroduction();
    }

    public void updateProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void addEmoji(Reactions reaction){
        this.reactionList.add(reaction);
    }
}
