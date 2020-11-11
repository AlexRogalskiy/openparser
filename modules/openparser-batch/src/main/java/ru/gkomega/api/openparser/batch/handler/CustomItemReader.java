package ru.gkomega.api.openparser.batch.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.lang.Nullable;

import java.util.List;

@RequiredArgsConstructor
public class CustomItemReader<T> implements ItemReader<T> {

    private final List<T> items;

    @Nullable
    @Override
    public T read() {
        if (!this.items.isEmpty()) {
            return this.items.remove(0);
        }
        return null;
    }
}
