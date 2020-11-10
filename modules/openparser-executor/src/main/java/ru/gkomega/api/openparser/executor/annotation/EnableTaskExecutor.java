package ru.gkomega.api.openparser.executor.annotation;

import org.springframework.context.annotation.Import;
import ru.gkomega.api.openparser.executor.configuration.TaskExecutorConfiguration;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@TypeQualifierDefault(ElementType.TYPE)
@Import(TaskExecutorConfiguration.class)
public @interface EnableTaskExecutor {
}
