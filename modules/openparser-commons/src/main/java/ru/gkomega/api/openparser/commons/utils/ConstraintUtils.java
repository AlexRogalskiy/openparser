package ru.gkomega.api.openparser.commons.utils;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.function.Predicate;

@UtilityClass
public class ConstraintUtils {
    /**
     * Validates input {@link Predicate} by input parameters
     *
     * @param <T>       type of validated value
     * @param predicate - initial input {@link Predicate} to validate by
     * @param value     - initial input {@link String} to validate
     * @param message   - initial input {@link String} validation message
     * @param context   - initial input {@link ConstraintValidatorContext}
     * @return true - if input {@code T} value is valid, false - otherwise
     */
    public static <T> boolean validate(final Predicate<T> predicate,
                                       final T value,
                                       final String message,
                                       final ConstraintValidatorContext context) {
        if (!predicate.test(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }

    /**
     * Returns in range {@link Predicate} by input min/max {@code T} bounds
     *
     * @param <T> type of range bound
     * @param min - initial input min {@code T} value
     * @param max - initial input max {@code T} value
     * @return in range {@link Predicate} by input min/max {@code T} bounds
     */
    @NonNull
    public static <T extends Comparable<? super T>> Predicate<T> inRange(final T min, final T max) {
        return c -> {
            if (Objects.isNull(min) && Objects.isNull(max)) {
                return true;
            }
            if (Objects.isNull(min)) {
                return c.compareTo(max) <= 0;
            }
            if (Objects.isNull(max)) {
                return c.compareTo(min) >= 0;
            }
            return c.compareTo(min) >= 0 && c.compareTo(max) <= 0;
        };
    }
}
