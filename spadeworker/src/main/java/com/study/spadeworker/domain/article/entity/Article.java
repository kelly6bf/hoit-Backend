package com.study.spadeworker.domain.article.entity;

import com.study.spadeworker.domain.board.entity.Board;
import com.study.spadeworker.domain.user.entity.User;
import com.study.spadeworker.global.config.audit.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt")
})
@Entity
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false, length = 3000)
    private String thumbnail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int likesCount;

    @Column(nullable = false)
    private int dislikesCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @OneToMany(mappedBy = "article")
    private List<ArticleComment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ArticleCategory articleCategory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Board board;

    @Builder
    public Article(String title, String description, String thumbnail, String content, User user, Board board, ArticleCategory articleCategory) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.content = content;
        this.likesCount = 0;
        this.dislikesCount = 0;
        this.user = user;
        this.articleCategory = articleCategory;
        this.board = board;
    }

    /**
     * 게시물 수정
     */
    public void update(String title, String description, String thumbnail, String content, ArticleCategory category) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.content = content;
        this.articleCategory = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
