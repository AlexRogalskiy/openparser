package ru.gkomega.api.openparser.batch.event.pubsub;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.batch.event.annotation.Step;
import ru.gkomega.api.openparser.batch.event.domain.StepExecutionData;
import ru.gkomega.api.openparser.batch.event.domain.StepExecutionEvent;
import ru.gkomega.api.openparser.batch.event.enumeration.StatusType;

/**
 * Wraps calls for methods taking {@link StepExecution} as an argument and
 * publishes notifications in the form of {@link org.springframework.context.ApplicationEvent}.
 */
@Aspect
@Component
public class StepExecutionEventPublisher implements ApplicationEventPublisherAware {

    /**
     * Application event publisher instance
     */
    private ApplicationEventPublisher applicationEventPublisher;

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
     */
    @Override
    public void setApplicationEventPublisher(@NonNull final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Pointcut("@annotation(annotation) || @within(annotation)")
    public void withStepAnnotation(final Step annotation) {
    }

    @Before(value = "withStepAnnotation(annotation)", argNames = "joinPoint,annotation")
    public void before(final JoinPoint joinPoint, final Step annotation) {
        final String message = String.format(
            "Before: [%s], with key: [%s], value: [%s], status: [%s]",
            joinPoint.toShortString(),
            annotation.key(),
            annotation.value(),
            annotation.status()
        );
        this.publish(joinPoint.getTarget(), StepExecutionData.of(message, StatusType.INITIALIZED));
    }

    @After(value = "withStepAnnotation(annotation)", argNames = "joinPoint,annotation")
    public void after(final JoinPoint joinPoint, final Step annotation) {
        final String message = String.format(
            "After: [%s], with key: [%s], value: [%s], status: [%s]",
            joinPoint.toShortString(),
            annotation.key(),
            annotation.value(),
            annotation.status()
        );
        this.publish(joinPoint.getTarget(), StepExecutionData.of(message, StatusType.REGISTERED));
    }

    @AfterThrowing(pointcut = "withStepAnnotation(annotation)", argNames = "joinPoint,annotation,e", throwing = "e")
    public void onError(final JoinPoint joinPoint, final Step annotation, final Throwable e) {
        final String message = String.format(
            "Error in: [%s], with key: [%s], value: [%s], status: [%s], message: [%s]",
            joinPoint.toShortString(),
            annotation.key(),
            annotation.value(),
            annotation.status(),
            e.getMessage()
        );
        this.publish(joinPoint.getTarget(), StepExecutionData.of(message, StatusType.INVALID));
    }

    /*
     * Publish a {@link SimpleMessageApplicationEvent} with the given
     * parameters.
     */
    private void publish(final Object source, final StepExecutionData message) {
        this.applicationEventPublisher.publishEvent(new StepExecutionEvent(source, message));
    }
}
