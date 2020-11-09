package ru.gkomega.api.openparser.batch.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@StepScope
@RequiredArgsConstructor
public class ItemListWriter<T> implements ItemWriter<List<T>> {

    private final ItemWriter<T> wrapped;

    @Override
    public void write(final List<? extends List<T>> items) throws Exception {
        for (final List<T> subList : items) {
            this.wrapped.write(subList);
        }
    }
}
