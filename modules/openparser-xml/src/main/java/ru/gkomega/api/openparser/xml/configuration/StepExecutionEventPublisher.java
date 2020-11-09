package ru.gkomega.api.openparser.xml.configuration;

import org.aspectj.lang.JoinPoint;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.xml.enumeration.StatusType;
import ru.gkomega.api.openparser.xml.model.domain.StepExecutionData;
import ru.gkomega.api.openparser.xml.model.domain.StepExecutionEvent;

/**
 * Wraps calls for methods taking {@link StepExecution} as an argument and
 * publishes notifications in the form of {@link org.springframework.context.ApplicationEvent}.
 */
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

    public void before(final JoinPoint joinPoint,
                       final StepExecution stepExecution) {
        final String msg = "Before: " + joinPoint.toShortString() + " with: " + stepExecution;
        this.publish(joinPoint.getTarget(), StepExecutionData.of(msg, StatusType.INITIALIZED));
    }

    public void after(final JoinPoint joinPoint,
                      final StepExecution stepExecution) {
        final String msg = "After: " + joinPoint.toShortString() + " with: " + stepExecution;
        this.publish(joinPoint.getTarget(), StepExecutionData.of(msg, StatusType.REGISTERED));
    }

    public void onError(final JoinPoint joinPoint,
                        final StepExecution stepExecution,
                        final Throwable t) {
        final String msg = "Error in: " + joinPoint.toShortString() + " with: " + stepExecution + " (" + t.getClass() + ":" + t.getMessage() + ")";
        this.publish(joinPoint.getTarget(), StepExecutionData.of(msg, StatusType.INVALID));
    }

    /*
     * Publish a {@link SimpleMessageApplicationEvent} with the given
     * parameters.
     */
    private void publish(final Object source,
                         final StepExecutionData message) {
        this.applicationEventPublisher.publishEvent(new StepExecutionEvent(source, message));
    }
}
