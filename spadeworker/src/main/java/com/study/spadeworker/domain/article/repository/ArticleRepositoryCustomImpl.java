package com.study.spadeworker.domain.article.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.spadeworker.domain.article.constant.OrderType;
import com.study.spadeworker.domain.article.dto.article.ArticlesViewOptionDto;
import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.article.entity.QArticle;
import com.study.spadeworker.domain.article.entity.QArticleHashtag;
import com.study.spadeworker.global.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport
        implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        super(Article.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Article> getArticlesByBoard(
            Long boardId,
            ArticlesViewOptionDto articlesViewOptionDto,
            Pageable pageable
    ) {
        QArticle article = QArticle.article;
        QArticleHashtag articleHashtag = QArticleHashtag.articleHashtag;

        List<Article> articles = queryFactory.selectFrom(article)
                .leftJoin(articleHashtag).on(articleHashtag.article.eq(article))
                .where(
                        QArticle.article.board.id.eq(boardId),
                        searchByKeyword(articlesViewOptionDto.getKeyword()),
                        checkAllHashtag(articlesViewOptionDto.getHashtag())
                )
                .distinct()
                .orderBy(getOrderOption(OrderType.valueOf(articlesViewOptionDto.getOrder().toUpperCase())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(articles);
    }

    // 정렬 옵션에 따른 정렬
    private OrderSpecifier<?> getOrderOption(OrderType orderType) {
        QArticle article = QArticle.article;
        if (orderType == null || orderType == OrderType.RECENT) {
            return article.createdAt.desc();
        } else {
            return switch (orderType) {
                case COMMENT -> article.comments.size().desc();
                case LIKES -> article.likesCount.desc();
                default -> article.createdAt.desc();
            };
        }
    }

    // 검색어로 검색
    private BooleanExpression searchByKeyword(String keyword) {
        QArticle article = QArticle.article;
        return keyword == null ? null :
                article.title.like("%" + keyword + "%")
                        .or(article.content.like("%" + keyword + "%"));
    }

    // Hashtag 검색
    private BooleanExpression searchByHashtag(String hashtag) {
        QArticleHashtag articleHashtag = QArticleHashtag.articleHashtag;
        return hashtag == null ? null :
                QArticle.article.id.in(
                        queryFactory.select(
                                articleHashtag.article.id
                        ).from(articleHashtag).where(articleHashtag.hashtag.title.eq(hashtag)));
    }

    // 여러개의 해시태그가 모두 포함되어있는지 검사하는 Builder
    private BooleanBuilder checkAllHashtag(String hashtag) {
        List<String> hashtags = StringUtil.splitParameter(hashtag, ",");
        BooleanBuilder builder = new BooleanBuilder();
        if (hashtags != null && hashtags.size() != 0) {
            for (String tag : hashtags) {
                builder.and(searchByHashtag(tag));
            }
        }
        return builder;
    }
}

