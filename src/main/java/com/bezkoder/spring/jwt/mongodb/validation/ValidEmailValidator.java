package com.bezkoder.spring.jwt.mongodb.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    // reasonable regex for typical emails (not perfect but practical)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Let other annotations handle null/blank (e.g. @NotBlank). If null/blank, treat as valid here.
        if (value == null || value.isBlank()) {
            return true;
        }
        return EMAIL_PATTERN.matcher(value).matches();
    }
}
