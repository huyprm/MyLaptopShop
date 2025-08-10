package com.ptithcm2021.laptopshop.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s==null|| s.isEmpty()){
            return false;
        }
        return s.matches("^0\\d{9}$");
    }
}
