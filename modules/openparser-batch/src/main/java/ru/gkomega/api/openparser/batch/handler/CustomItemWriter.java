package ru.gkomega.api.openparser.batch.handler;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.TransactionAwareProxyFactory;
import org.springframework.lang.NonNull;

import java.util.List;

public class CustomItemWriter<T> implements ItemWriter<T> {

    private final List<T> output = TransactionAwareProxyFactory.createTransactionalList();

    public void write(@NonNull final List<? extends T> items) {
        this.output.addAll(items);
    }

    public List<T> getOutput() {
        return this.output;
    }
}
