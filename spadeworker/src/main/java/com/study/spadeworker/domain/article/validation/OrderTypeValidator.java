package com.study.spadeworker.domain.article.validation;

import com.study.spadeworker.domain.article.constant.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class OrderTypeValidator implements ConstraintValidator<OrderTypeValid, String> {

    @Override
    public boolean isValid(String order, ConstraintValidatorContext context) {
        boolean valid = true;

        try {
            OrderType.valueOf(order.toUpperCase());
        } catch (Exception e) {
            valid = false;
        }

        return valid;
    }

    @Override
    public void initialize(OrderTypeValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}

