package ru.gkomega.api.openparser.batch.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.gkomega.api.openparser.batch.property.ResourceProperty;

import java.io.IOException;

@Slf4j
public class MultiResourceReader<T> extends MultiResourceItemReader<T> {

    public MultiResourceReader(final ResourceProperty resourceProperty,
                               final ResourceAwareItemReaderItemStream<T> delegate) {
        try {
            final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            final Resource[] resources = patternResolver.getResources(resourceProperty.getPathPattern());

            this.setResources(resources);
            this.setDelegate(delegate);
            this.setSaveState(false);
        } catch (IOException ex) {
            log.error("Cannot initialize resource by pattern: {}", resourceProperty.getPathPattern(), ex);
        }
    }
}
