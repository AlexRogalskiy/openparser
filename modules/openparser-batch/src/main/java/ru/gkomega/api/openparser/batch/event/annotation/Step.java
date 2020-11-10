package ru.gkomega.api.openparser.batch.event.annotation;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TypeQualifierDefault(ElementType.METHOD)
public @interface Step {
    /**
     * Returns {@link String} key description
     *
     * @return {@link String} key description
     */
    String key();

    /**
     * Returns {@link Status} status description
     *
     * @return {@link Status} status description
     */
    Status status() default Status.ACTIVE;

    /**
     * Returns {@link String} step description
     *
     * @return {@link String} step description
     */
    String value() default "";

    /**
     * Default step status {@link Enum} type
     */
    enum Status {
        ACTIVE,
        DISABLED,
        BLOCKED,
        FAILED
    }
}
