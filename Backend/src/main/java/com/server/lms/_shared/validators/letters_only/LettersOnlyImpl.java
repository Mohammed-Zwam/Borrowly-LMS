package com.server.lms._shared.validators.letters_only;

import jakarta.validation.ConstraintValidator;

public class LettersOnlyImpl implements ConstraintValidator<LettersOnly, String> {
    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Consider null or empty as valid, use @NotBlank for non-empty validation
        }
        return value.matches("^[a-zA-Z]+$");
    }
}
