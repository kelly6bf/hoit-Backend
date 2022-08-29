package com.study.spadeworker.domain.article.service;

import com.study.spadeworker.domain.article.entity.Article;
import com.study.spadeworker.domain.article.entity.ArticleHashtag;
import com.study.spadeworker.domain.article.entity.Hashtag;
import com.study.spadeworker.domain.article.repository.ArticleHashtagRepository;
import com.study.spadeworker.domain.article.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagRepository articleHashtagRepository;

    public void saveArticleHashtag(List<String> hashtagList, Article article) {
        for (String hashtag : hashtagList) {
            if (!hashtagRepository.existsHashtagByTitle(hashtag)) {
                createHashtag(hashtag);
            }

            articleHashtagRepository.save(
                    ArticleHashtag.builder()
                            .article(article)
                            .hashtag(hashtagRepository.findByTitle(hashtag))
                            .build()
            );
        }
    }

    public void updateArticleHashtag(List<String> hashtagList, Article article) {
        deleteArticleHashtag(article);
        saveArticleHashtag(hashtagList, article);
    }

    private void createHashtag(String hashtag) {
        hashtagRepository.saveAndFlush(
                new Hashtag(hashtag)
        );
    }

    public void deleteArticleHashtag(Article article) {
        articleHashtagRepository.deleteArticleHashtagsByArticle(article);
    }

    @Transactional(readOnly = true)
    public List<String> getArticleHashtagList(Article article) {
        List<ArticleHashtag> articleHashtagList = articleHashtagRepository.findAllByArticle(article);
        List<String> hashtagList = new ArrayList<>();

        for (ArticleHashtag ah : articleHashtagList) {
            hashtagList.add(ah.getHashtag().getTitle());
        }

        return hashtagList;
    }
}
