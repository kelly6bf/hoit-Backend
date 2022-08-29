package com.study.spadeworker.domain.article.entity;

import com.study.spadeworker.domain.user.entity.User;
import com.study.spadeworker.global.config.audit.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleComment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private int likesCount;

    @Column(nullable = false)
    private int dislikesCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    private ArticleComment(String content, Article article, User user) {
        this.content = content;
        this.likesCount = 0;
        this.dislikesCount = 0;
        this.article = article;
        this.user = user;
    }

    public static ArticleComment of(String content, Article article, User user) {
        return new ArticleComment(content, article, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
