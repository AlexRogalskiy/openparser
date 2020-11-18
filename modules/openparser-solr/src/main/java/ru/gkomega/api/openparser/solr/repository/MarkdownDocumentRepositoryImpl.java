package ru.gkomega.api.openparser.solr.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Repository;
import ru.gkomega.api.openparser.solr.model.entity.MarkdownDocumentEntity;

@Repository
@RequiredArgsConstructor
public class MarkdownDocumentRepositoryImpl implements CustomMarkdownDocumentRepository {

    private final SolrTemplate solrTemplate;

    @Override
    public HighlightPage<MarkdownDocumentEntity> findDocuments(final String searchTerm, final Pageable page) {
        final Criteria fileIdCriteria = new Criteria(MarkdownDocumentEntity.FILE_ID_FIELD).boost(2).is(searchTerm);
        final Criteria contentCriteria = new Criteria(MarkdownDocumentEntity.CONTENT_FIELD).fuzzy(searchTerm);
        final SimpleHighlightQuery query = new SimpleHighlightQuery(fileIdCriteria.or(contentCriteria), page);
        query.setHighlightOptions(new HighlightOptions()
            .setSimplePrefix("<strong>")
            .setSimplePostfix("</strong>")
            .addField(MarkdownDocumentEntity.FILE_ID_FIELD, MarkdownDocumentEntity.CONTENT_FIELD));

        return this.solrTemplate.queryForHighlightPage(MarkdownDocumentEntity.MARKDOWN_CORE, query, MarkdownDocumentEntity.class);
    }
}
