package ru.gkomega.api.openparser.batch.handler;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

public class ResourcePassThroughReader implements ResourceAwareItemReaderItemStream<Resource> {

    private Resource resource;
    private boolean read = false;

    @Override
    public void setResource(@NonNull final Resource resource) {
        this.resource = resource;
        this.read = false;
    }

    @Override
    public Resource read() {
        if (this.read) {
            return null;
        } else {
            this.read = true;
            return this.resource;
        }
    }

    @Override
    public void open(@NonNull final ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void update(@NonNull final ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
    }
}
