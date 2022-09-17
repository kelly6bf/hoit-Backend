package com.study.spadeworker.domain.article.validation;

import com.study.spadeworker.domain.article.repository.ArticleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ArticleCategoryValidator implements ConstraintValidator<ArticleCategoryValid, String> {

    private final ArticleCategoryRepository articleCategoryRepository;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        boolean isValid = articleCategoryRepository.existsByTitle(title);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("잘못된 게시글 카테고리입니다.")
                    .addConstraintViolation();
        }

        return isValid;
    }

    @Override
    public void initialize(ArticleCategoryValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
