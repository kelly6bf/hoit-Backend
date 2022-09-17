package com.study.spadeworker.domain.article.validation;

import com.study.spadeworker.domain.article.constant.OrderType;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class OrderTypeValidator implements ConstraintValidator<OrderTypeValid, String> {

    @Override
    public boolean isValid(String order, ConstraintValidatorContext context) {
        boolean valid = true;

        if (order == null)
            return valid;

        try {
            OrderType.valueOf(order.toUpperCase());
        } catch (Exception e) {
            valid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("잘못된 정렬 옵션입니다.")
                    .addConstraintViolation();
        }

        return valid;
    }

    @Override
    public void initialize(OrderTypeValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}

