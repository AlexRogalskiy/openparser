package ru.gkomega.api.openparser.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

/**
 * Dummy {@link ItemProcessor} which only logs data it receives.
 */
@Slf4j
public class LogItemProcessor<T> implements ItemProcessor<T, T> {
    /**
     * @see ItemProcessor#process(Object)
     */
    @Override
    public T process(@NonNull final T item) {
        log.info(">>> Job processing item: {}", item);
        return item;
    }
}
