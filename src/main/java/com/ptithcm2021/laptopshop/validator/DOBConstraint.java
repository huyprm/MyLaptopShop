package com.ptithcm2021.laptopshop.validator;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DOBValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
public @interface DOBConstraint {
    String message() default "Invalid date of birth, incorrect format dd/MM/yyyy";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
