package ru.practicum.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.practicum.constants.constants.DATE_FORMAT;

@Target({ElementType.PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DateValidatorInner.class})
public @interface DateValidator {
    String message() default "Формат даты не соответствует {dateFormat}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String dateFormat() default DATE_FORMAT;
}
