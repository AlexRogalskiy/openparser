package ru.gkomega.api.openparser.solr.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.ContentStreamBase;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.solr.model.entity.MarkdownDocumentEntity;
import ru.gkomega.api.openparser.solr.exception.SolrItemWriterException;
import ru.gkomega.api.openparser.solr.model.domain.HtmlResource;
import ru.gkomega.api.openparser.solr.property.MarkdownSolrResourceProperty;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarkdownDocumentWriter implements ItemWriter<HtmlResource> {
    private static final String FILE_ID_LITERAL = "literal.file.id";

    private final SolrClient solrClient;
    private final MarkdownSolrResourceProperty solrResourceProperty;

    @Override
    public void write(final List<? extends HtmlResource> list) {
        list.stream().map(this::updateRequest).forEach(this::request);
    }

    private ContentStreamUpdateRequest updateRequest(final HtmlResource htmlFile) {
        try {
            final ContentStreamUpdateRequest updateRequest = new ContentStreamUpdateRequest(this.solrResourceProperty.getExtractPath());
            updateRequest.addContentStream(new ContentStreamBase.StringStream(htmlFile.getHtml(), "text/html;charset=UTF-8"));
            updateRequest.setParam(FILE_ID_LITERAL, htmlFile.getResource().getFile().getAbsolutePath());
            updateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
            return updateRequest;
        } catch (IOException ex) {
            throw new SolrItemWriterException("Could not retrieve filename", ex);
        }
    }

    private void request(final ContentStreamUpdateRequest updateRequest) {
        try {
            this.solrClient.request(updateRequest, MarkdownDocumentEntity.MARKDOWN_CORE);
            log.info("Updated document in Solr: {}", updateRequest.getParams().get(FILE_ID_LITERAL));
        } catch (SolrServerException | IOException ex) {
            throw new SolrItemWriterException("Could not index document", ex);
        }
    }
}
