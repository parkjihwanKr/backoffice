package com.example.backoffice.domain.member.entity;

import com.example.backoffice.domain.reaction.entity.Reactions;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column
    @Enumerated(EnumType.STRING)
    private MemberPosition position;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberDepartment department;

    // 급여
    private Long salary;

    // 휴가, 해당 부분은 21억 넘을 이유 없음.
    private Integer vacationDays;

    // 휴가 상태
    private Boolean onVacation;

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

    public void updateAttribute(
            MemberRole role, MemberDepartment department, MemberPosition position){
        this.role = role;
        this.department = department;
        this.position = position;
    }

    public void updateSalary(Long salary){
        this.salary = salary;
    }

    public void addEmoji(Reactions reaction){
        this.reactionList.add(reaction);
        this.loveCount++;
    }

    public void deleteEmoji(){
        this.loveCount--;
    }
}
