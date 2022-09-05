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

    @Column(nullable = false)
    private Boolean isChild;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private ArticleComment parentComment;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    private User recipient;

    private ArticleComment(String content, Boolean isChild, Article article, User user, ArticleComment parentComment, User recipient) {
        this.content = content;
        this.likesCount = 0;
        this.dislikesCount = 0;
        this.isChild = isChild;
        this.article = article;
        this.user = user;
        this.parentComment = parentComment;
        this.recipient = recipient;
    }

    /**
     * 최상위 댓글 작성
     */
    public static ArticleComment createRootComment(String content, Article article, User user) {
        return new ArticleComment(content, false, article, user, null, null);
    }

    /**
     * 대댓글 작성
     */
    public static ArticleComment createChildComment(String content, Article article, User user, ArticleComment parentComment, User recipient) {
        return new ArticleComment(content, true, article, user, parentComment, recipient);
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
