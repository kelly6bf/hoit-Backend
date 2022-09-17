package com.study.spadeworker.domain.article.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OrderTypeValidator.class)
@Target({
        ElementType.METHOD,
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderTypeValid {

    String message() default "잘못된 정렬 옵션입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
