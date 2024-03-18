package com.example.backoffice.domain.member.entity;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.MemberRole;
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
    private String name;

    // 게임 접속 아이디
    private String memberName;

    private String password;

    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String address;

    private String contact;

    private String profileImageUrl;

    public void updateMemberInfo(MembersRequestDto.UpdateMemberRequestDto requestDto, String bCrytPassword){
        this.memberName = requestDto.getMemberName();
        this.password = bCrytPassword;
        this.email = requestDto.getEmail();
        this.address = requestDto.getAddress();
        this.contact = requestDto.getContact();
    }

    public void updateRole(MemberRole role){
        this.role = role;
    }

    public void updateProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
