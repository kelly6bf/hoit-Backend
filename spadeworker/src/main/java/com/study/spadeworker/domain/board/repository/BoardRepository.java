package com.study.spadeworker.domain.board.repository;

import com.study.spadeworker.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
