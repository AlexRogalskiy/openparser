package ru.gkomega.api.openparser.batch.handler;

import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;

import java.util.List;

public class NoOpItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(@NonNull final List<? extends T> items) {
    }
}
