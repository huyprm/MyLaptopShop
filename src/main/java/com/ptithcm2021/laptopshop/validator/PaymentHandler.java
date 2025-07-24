package com.ptithcm2021.laptopshop.validator;

import com.ptithcm2021.laptopshop.model.enums.PaymentMethodEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface PaymentHandler {
    PaymentMethodEnum value();
}
