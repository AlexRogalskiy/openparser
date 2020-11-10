package ru.gkomega.api.openparser.commons.annotation;

import org.springframework.util.Assert;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Pattern;

import static ru.gkomega.api.openparser.commons.utils.ConstraintUtils.inRange;
import static ru.gkomega.api.openparser.commons.utils.ConstraintUtils.validate;

/**
 * {@link Guid} {@link ConstraintValidator} implementation
 */
public class GuidConstraintValidator implements ConstraintValidator<Guid, String> {
    /**
     * Default {@link String} message
     */
    private String message;
    /**
     * Default {@link String} pattern
     */
    private String pattern;
    /**
     * Default {@code int} min length
     */
    private int minLength;
    /**
     * Default {@code int} max length
     */
    private int maxLength;

    /**
     * Initializes validator by input {@link Guid} annotation parameters
     *
     * @param constraintAnnotation - initial input {@link Guid} annotation
     */
    @Override
    public void initialize(final Guid constraintAnnotation) {
        Assert.isTrue(constraintAnnotation.minLength() >= 0, "minLength");
        Assert.isTrue(constraintAnnotation.maxLength() >= 0, "maxLength");

        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.message = constraintAnnotation.message();
        this.pattern = constraintAnnotation.pattern();
    }

    /**
     * Validates input {@link String} value by {@link ConstraintValidatorContext}
     *
     * @param value   - initial input {@link String} to validate
     * @param context - initial input {@link ConstraintValidatorContext}
     * @return true - if input {@link String} is valid, false - otherwise
     */
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
            .map(v -> validate(vv -> Pattern.matches(this.pattern, vv), v, this.message, context)
                && validate(s -> inRange(this.minLength, this.maxLength).test(s.length()), v, this.message, context)
            )
            .orElse(true);
    }
}
