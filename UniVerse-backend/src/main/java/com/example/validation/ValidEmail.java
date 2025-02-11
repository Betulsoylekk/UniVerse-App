package com.example.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom annotation for email validation
@Target({ ElementType.FIELD, ElementType.PARAMETER })  // Can be used on fields or parameters
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)  // Specifies the validator class
public @interface ValidEmail {
    String message() default "Invalid email format";  // Default error message
    Class<?>[] groups() default {};  // Used for grouping constraints
    Class<? extends Payload>[] payload() default {};  // Can be used to carry metadata
}