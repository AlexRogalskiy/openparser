package ru.gkomega.api.openparser.batch.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.gkomega.api.openparser.batch.property.ResourceProperty;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RequiredArgsConstructor
public class MultiResourceReader<T> extends MultiResourceItemReader<T> {

    private final ResourceProperty resourceProperty;
    private final ResourceAwareItemReaderItemStream<T> delegate;

    @PostConstruct
    private void initialize() throws IOException {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = patternResolver.getResources(this.resourceProperty.getPathPattern());

        this.setResources(resources);
        this.setDelegate(this.delegate);
    }
}
