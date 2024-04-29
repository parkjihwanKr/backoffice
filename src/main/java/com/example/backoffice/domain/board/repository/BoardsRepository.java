package com.example.backoffice.domain.board.repository;

import com.example.backoffice.domain.board.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Boards, Long> {
    //Page<Boards> findBoardsByCreatedAt(Pageable pageable);
}
