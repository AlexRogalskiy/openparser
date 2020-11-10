package ru.gkomega.api.openparser.batch.event.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.batch.event.domain.StepExecutionEvent;

import java.time.Instant;

@Slf4j
@Component
public class StepExecutionEventListener {

    /**
     * Handles {@link StepExecutionEvent} by changing current status
     *
     * @param documentEvent initial input {@link StepExecutionEvent} to handle
     */
    @Async
    @EventListener
    public void handleMessageEvent(final StepExecutionEvent documentEvent) {
        this.logEvent(documentEvent);
    }

    /**
     * Describes input {@link StepExecutionEvent} with logging instance
     *
     * @param event initial input {@link StepExecutionEvent} to log
     */
    private void logEvent(final StepExecutionEvent event) {
        log.info(
            ">>> Source: [{}] >>> handling [{}]: >>> status: [{}], timestamp: [{}], message: [{}]",
            event.getSource().getClass().getSimpleName(),
            event.getClass().getSimpleName(),
            event.getMessageData().getStatus(),
            Instant.ofEpochMilli(event.getTimestamp()),
            event.getMessageData().getMessage()
        );
    }
}
