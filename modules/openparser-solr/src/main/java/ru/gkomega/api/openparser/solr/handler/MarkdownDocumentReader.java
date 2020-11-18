package ru.gkomega.api.openparser.solr.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.solr.property.MarkdownSolrResourceProperty;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MarkdownDocumentReader extends MultiResourceItemReader<Resource> {

    private final MarkdownSolrResourceProperty solrResourceProperty;

    @PostConstruct
    public void initialize() throws IOException {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = patternResolver.getResources(this.solrResourceProperty.getPathPattern());

        this.setResources(resources);
        this.setDelegate(new ResourcePassthroughReader());
    }
}
