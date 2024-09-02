package com.example.backoffice.domain.board.repository;

import com.example.backoffice.domain.board.entity.BoardType;
import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {
    Page<Boards> findAllByBoardType(Pageable pageable, BoardType boardType);

    Page<Boards> findAllByDepartment(Pageable pageable, MemberDepartment department);

    Optional<Boards> findByIdAndDepartment(Long boardId, MemberDepartment department);
}
