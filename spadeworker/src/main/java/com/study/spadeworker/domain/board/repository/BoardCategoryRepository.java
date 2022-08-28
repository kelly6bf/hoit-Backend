package com.study.spadeworker.domain.board.repository;

import com.study.spadeworker.domain.board.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
}
