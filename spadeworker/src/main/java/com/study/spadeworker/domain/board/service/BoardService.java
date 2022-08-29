package com.study.spadeworker.domain.board.service;

import com.study.spadeworker.domain.board.entity.Board;
import com.study.spadeworker.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시판 id를 통해 게시판 Entity 조회
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 게시판이 존재하지 않습니다."));
    }
}
