package com.example.backoffice.domain.board.repository;

import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {
    // isImportant가 true인 게시글 중에서 최신순으로 3개 가져오기
    List<Boards> findTop3ByIsImportantTrueOrderByModifiedAtDesc();

    // isImportant가 false인 게시글들을 최신순으로 가져오기
    Page<Boards> findByIsImportantFalseOrderByModifiedAtDesc(Pageable pageable);

    Page<Boards> findAllByBoardType(Pageable pageable, BoardType boardType);

    Page<Boards> findAllByDepartment(Pageable pageable, MemberDepartment department);

    Optional<Boards> findByIdAndDepartment(Long boardId, MemberDepartment department);
}
