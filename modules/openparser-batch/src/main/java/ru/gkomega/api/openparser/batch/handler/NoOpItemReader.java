package ru.gkomega.api.openparser.batch.handler;

import org.springframework.batch.item.ItemReader;

public class NoOpItemReader<T> implements ItemReader<T> {

    @Override
    public T read() {
        return null;
    }
}
