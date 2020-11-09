package ru.gkomega.api.openparser.commons.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Documented
@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "\\d{10}$|\\d{12}$")
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface Inn {
    /**
     * Default constraint message
     *
     * @return {@link String} constraint message
     */
    String message() default "{ru.gkomega.api.openparser.commons.annotation.Inn.message}";

    /**
     * Default constraint groups
     *
     * @return {@link Class} array of constrains groups
     */
    Class<?>[] groups() default {};

    /**
     * Default constraint payloads
     *
     * @return {@link Class} array of {@link Payload}s
     */
    Class<? extends Payload>[] payload() default {};
}
