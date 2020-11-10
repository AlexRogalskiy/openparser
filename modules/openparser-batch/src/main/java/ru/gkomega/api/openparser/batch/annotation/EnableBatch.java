package ru.gkomega.api.openparser.batch.annotation;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import ru.gkomega.api.openparser.batch.configuration.BatchConfiguration;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableBatchProcessing
@TypeQualifierDefault(ElementType.TYPE)
@Import(BatchConfiguration.class)
public @interface EnableBatch {
    /**
     * Enable/disable modularized configuration into multiple application contexts
     *
     * @return true - if modularized configuration enabled, false - otherwise
     */
    @AliasFor(annotation = EnableBatchProcessing.class, attribute = "modular")
    boolean modular() default false;
}
