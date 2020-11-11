package ru.gkomega.api.openparser.batch.handler;

import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.gkomega.api.openparser.batch.property.ResourceProperty;

public class MultiResourceWriter<T> extends MultiResourceItemWriter<T> {

    public MultiResourceWriter(final ResourceProperty resourceProperty,
                               final ResourceAwareItemWriterItemStream<T> delegate) {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource resource = patternResolver.getResource(resourceProperty.getPathPattern());

        this.setResource(resource);
        this.setDelegate(delegate);
        this.setSaveState(false);
    }
}
