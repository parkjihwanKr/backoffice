package com.example.backoffice.domain.event.exception.annotation;

import com.example.backoffice.domain.event.exception.validator.UrgentReasonValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UrgentReasonValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrgentReasonRequired {

    String message() default "Reason must be provided if urgent is true";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
