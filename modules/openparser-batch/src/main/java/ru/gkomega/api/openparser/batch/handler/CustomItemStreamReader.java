package ru.gkomega.api.openparser.batch.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class CustomItemStreamReader<T> implements ItemReader<T>, ItemStream {

    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private static final String CURRENT_INDEX = "current.index";

    private final List<T> items;

    public T read() {
        if (this.currentIndex.get() < this.items.size()) {
            return this.items.get(this.currentIndex.getAndDecrement());
        }
        return null;
    }

    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        if (executionContext.containsKey(CURRENT_INDEX)) {
            this.currentIndex.set(Long.valueOf(executionContext.getLong(CURRENT_INDEX)).intValue());
        } else {
            this.currentIndex.set(0);
        }
    }

    public void update(final ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putLong(CURRENT_INDEX, this.currentIndex.get());
    }

    public void close() throws ItemStreamException {
    }
}
