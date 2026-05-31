package com.server.lms._shared.validators.letters_only;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = { LettersOnlyImpl.class })
@Retention(RUNTIME)
public @interface LettersOnly {
    String message() default "{custom-validators.letters-only.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
