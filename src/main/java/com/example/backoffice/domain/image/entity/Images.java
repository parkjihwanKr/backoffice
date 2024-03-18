package com.example.backoffice.domain.image.entity;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.Members;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    // profileImageUrl은 image : member = 1:1
    @OneToOne
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Boards board;
}
