package com.ptithcm2021.laptopshop.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.datetime.DateFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

@Slf4j
public class DOBValidator implements ConstraintValidator<DOBConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s==null|| s.length() == 0)return false;
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(s.trim(), dateFormat);
            return true;
        } catch (DateTimeParseException ex) {
            log.info((ex.getMessage()));
            return false;
        }
    }
}
