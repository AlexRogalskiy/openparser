package ru.gkomega.api.openparser.executor.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.gkomega.api.openparser.executor.configuration.TaskSchedulerConfiguration;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableScheduling
@TypeQualifierDefault(ElementType.TYPE)
@Import(TaskSchedulerConfiguration.class)
public @interface EnableTaskScheduler {
}
