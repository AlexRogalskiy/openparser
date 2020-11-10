package ru.gkomega.api.openparser.batch.event.annotation;

import org.springframework.context.annotation.Import;
import ru.gkomega.api.openparser.batch.event.pubsub.StepExecutionEventListener;
import ru.gkomega.api.openparser.batch.event.pubsub.StepExecutionEventPublisher;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@TypeQualifierDefault(ElementType.TYPE)
@Import({StepExecutionEventListener.class, StepExecutionEventPublisher.class})
public @interface EnableStepLog {
}
