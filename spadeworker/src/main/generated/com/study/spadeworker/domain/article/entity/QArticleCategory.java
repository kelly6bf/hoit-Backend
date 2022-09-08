package com.study.spadeworker.domain.article.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QArticleCategory is a Querydsl query type for ArticleCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticleCategory extends EntityPathBase<ArticleCategory> {

    private static final long serialVersionUID = 1018522414L;

    public static final QArticleCategory articleCategory = new QArticleCategory("articleCategory");

    public final com.study.spadeworker.global.config.audit.QBaseTimeEntity _super = new com.study.spadeworker.global.config.audit.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath title = createString("title");

    public QArticleCategory(String variable) {
        super(ArticleCategory.class, forVariable(variable));
    }

    public QArticleCategory(Path<? extends ArticleCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QArticleCategory(PathMetadata metadata) {
        super(ArticleCategory.class, metadata);
    }

}

