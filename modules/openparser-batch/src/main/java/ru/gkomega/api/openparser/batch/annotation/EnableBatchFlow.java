package ru.gkomega.api.openparser.batch.annotation;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.integration.config.EnableIntegration;
import ru.gkomega.api.openparser.batch.configuration.BatchIntegrationConfiguration;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableIntegration
@EnableBatchIntegration
@EnableBatchProcessing
@TypeQualifierDefault(ElementType.TYPE)
@Import(BatchIntegrationConfiguration.class)
public @interface EnableBatchFlow {
    /**
     * Enable/disable modularized configuration into multiple application contexts
     *
     * @return true - if modularized configuration enabled, false - otherwise
     */
    @AliasFor(annotation = EnableBatchProcessing.class, attribute = "modular")
    boolean modular() default false;
}
