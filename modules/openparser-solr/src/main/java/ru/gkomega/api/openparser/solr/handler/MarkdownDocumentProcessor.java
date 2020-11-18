package ru.gkomega.api.openparser.solr.handler;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.solr.model.domain.HtmlResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Component
public class MarkdownDocumentProcessor implements ItemProcessor<Resource, HtmlResource> {

    private Parser parser;
    private HtmlRenderer htmlRenderer;

    @PostConstruct
    private void initialize() {
        final List<Extension> extensions = Collections.singletonList(TablesExtension.create());
        this.parser = Parser.builder().extensions(extensions).build();
        this.htmlRenderer = HtmlRenderer.builder().extensions(extensions).build();
    }

    @Override
    public HtmlResource process(@NonNull final Resource markdownResource) throws IOException {
        try (final InputStreamReader reader = new InputStreamReader(markdownResource.getInputStream())) {
            final Node document = this.parser.parseReader(reader);
            return new HtmlResource(markdownResource, this.htmlRenderer.render(document));
        }
    }
}
