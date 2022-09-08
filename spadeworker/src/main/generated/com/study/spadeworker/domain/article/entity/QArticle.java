package com.study.spadeworker.domain.article.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = -1651811824L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final com.study.spadeworker.global.config.audit.QBaseEntity _super = new com.study.spadeworker.global.config.audit.QBaseEntity(this);

    public final QArticleCategory articleCategory;

    public final com.study.spadeworker.domain.board.entity.QBoard board;

    public final ListPath<ArticleComment, QArticleComment> comments = this.<ArticleComment, QArticleComment>createList("comments", ArticleComment.class, QArticleComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Integer> dislikesCount = createNumber("dislikesCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likesCount = createNumber("likesCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath title = createString("title");

    public final com.study.spadeworker.domain.user.entity.QUser user;

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.articleCategory = inits.isInitialized("articleCategory") ? new QArticleCategory(forProperty("articleCategory")) : null;
        this.board = inits.isInitialized("board") ? new com.study.spadeworker.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.user = inits.isInitialized("user") ? new com.study.spadeworker.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

