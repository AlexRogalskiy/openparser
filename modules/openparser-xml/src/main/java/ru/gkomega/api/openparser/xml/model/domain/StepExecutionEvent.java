package ru.gkomega.api.openparser.xml.model.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StepExecutionEvent extends ApplicationEvent {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 6096837088881466819L;

    /**
     * {@link StepExecutionData}
     */
    private final StepExecutionData messageData;

    /**
     * {@link StepExecutionEvent} constructor by input parameters
     *
     * @param source      - initial input source {@link Object}
     * @param messageData - initial input {@link StepExecutionData}
     */
    public StepExecutionEvent(final Object source,
                              final StepExecutionData messageData) {
        super(source);
        this.messageData = messageData;
    }
}
