package com.example.backoffice.domain.member.entity;

import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.favorite.entity.Favorites;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    // field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 실명
    @Column
    private String name;

    // 접속 아이디
    @Column(unique = true)
    @Pattern(
            regexp = "^[a-z0-9]{8,16}$",
            message = "아이디는 8자 이상 16자 이하의 소문자 알파벳와 숫자으로만 구성되어야 합니다."
    )
    private String memberName;

    @Column
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 60, message = "비밀번호 해시값은 최소 60자 이상이어야 합니다.")
    private String password;

    @Column(unique = true)
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(naver\\.com|gmail\\.com)$",
            message = "이메일은 naver.com 또는 gmail.com 도메인만 허용됩니다."
    )
    private String email;

    @Column(unique = true)
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "연락처는 '010-****-****' 형식이어야 합니다."
    )
    private String contact;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String address;

    private String profileImageUrl;

    @Column(length = 500)
    private String introduction;

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
    private Integer remainingVacationDays;

    // 휴가 상태
    private Boolean onVacation;

    // relations
    /*@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reactions> reactionList;*/

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Events> eventList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorites> favoritieList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembersEvaluations> membersEvaluations;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendances> attendanceList;

    // entity method
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

    public void addLoveCount(){
        this.loveCount++;
    }

    public void deleteLoveCount(){
        this.loveCount--;
    }

    public void updateOnVacation(Boolean onVacation){
        this.onVacation = onVacation;
    }

    public void updateRemainingVacation(){
        this.remainingVacationDays++;
    }

    public void plusRemainingVacation(int vacationDay){
        this.remainingVacationDays += vacationDay;
    }

    public void minusRemainingVacation(int vacationDay){
        this.remainingVacationDays -= vacationDay;
    }

    public void updateRemainingVacationDays (int vacationDay){
        this.remainingVacationDays = vacationDay;
    }

    public void updateRemainingVacationYearly(){
        this.remainingVacationDays += 5;
    }
}
