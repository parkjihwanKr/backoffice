package com.example.backoffice.domain.board.service;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardsServiceV1 {

    List<Boards> findByIsImportantTrueOrderByModifiedAtDesc();

    Page<Boards> findByIsImportantFalseOrderByCreatedAtDesc(Pageable pageable);

    Long getCommentListSize(Boards board);

    Page<Boards> findAllByDepartment(Pageable pageable, MemberDepartment department);

    Boards save(Boards board);

    Boards findByIdAndDepartment(Long boardId, MemberDepartment department);

    Boards findById(Long boardId);

    void deleteById(Long boardId);
}
