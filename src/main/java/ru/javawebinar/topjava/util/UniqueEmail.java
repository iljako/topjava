package ru.javawebinar.topjava.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "{error.duplicateEmail}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}