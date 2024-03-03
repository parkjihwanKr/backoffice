package com.example.backoffice.domain.member.entity;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Members extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

}
