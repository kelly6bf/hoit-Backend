package com.study.spadeworker.domain.article.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ArticleCategoryValidator.class)
@Target({
        ElementType.METHOD,
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArticleCategoryValid {

    String message() default "잘못된 게시글 카테고리입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
