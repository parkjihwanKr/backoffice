package com.example.backoffice.domain.favorite.entity;

import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;
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
@NoArgsConstructor
@AllArgsConstructor
public class Favorities extends CommonEntity {

    // fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 해당 도메인에 대한 제목 또는 내용
    private String content;

    // relations
    @Column
    @Enumerated(EnumType.STRING)
    private FavoriteType favoriteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    // entity methods
}
