package ru.gkomega.api.openparser.commons.annotation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Length;
import org.springframework.core.annotation.AliasFor;

import javax.annotation.meta.TypeQualifierNickname;
import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.lang.annotation.*;

/**
 * Guid pattern constraint annotation
 */
@Documented
@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER,
    ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@TypeQualifierNickname
@Repeatable(Guid.List.class)
@ConstraintComposition(CompositionType.OR)
@NullOrNotBlank
@Pattern(regexp = Guid.UUID_REGEX)
@Constraint(validatedBy = {GuidConstraintValidator.class})
public @interface Guid {
    /**
     * Uuid regex pattern
     */
    String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    @PositiveOrZero
    @OverridesAttribute(constraint = Length.class, name = "max")
    @AliasFor(annotation = Length.class, attribute = "max")
    int maxLength() default 64;

    @PositiveOrZero
    @OverridesAttribute(constraint = Length.class, name = "min")
    @AliasFor(annotation = Length.class, attribute = "min")
    int minLength() default 0;

    @OverridesAttribute(constraint = Pattern.class, name = "regexp")
    @AliasFor(annotation = Pattern.class, attribute = "regexp")
    String pattern() default UUID_REGEX;

    @OverridesAttribute(constraint = Pattern.class, name = "flags")
    @AliasFor(annotation = Pattern.class, attribute = "flags")
    Pattern.Flag[] patternFlags() default {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE};

    @OverridesAttribute(constraint = NotBlank.class, name = "message")
    @AliasFor(annotation = NotBlank.class, attribute = "message")
    String notBlankMessage() default "{model.constraint.uuid.notBlank}";

    @OverridesAttribute(constraint = Length.class, name = "message")
    @AliasFor(annotation = Length.class, attribute = "message")
    String lengthMessage() default "{model.constraint.uuid.length}";

    /**
     * Default constraint message
     *
     * @return {@link String} constraint message
     */
    String message() default "{ru.gkomega.api.openparser.commons.annotation.UUID.message}";

    /**
     * Default constraint groups
     *
     * @return {@link Class} array of constraints groups
     */
    Class<?>[] groups() default {};

    /**
     * Default constraint payloads
     *
     * @return {@link Class} array of {@link Payload}s
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link Guid} annotations on the same element.
     *
     * @see Guid
     */
    @Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /**
         * Returns array of {@link Guid}s
         *
         * @return array of {@link Guid}s
         */
        Guid[] value();
    }
}
