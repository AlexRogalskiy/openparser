package ru.gkomega.api.openparser.solr.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import ru.gkomega.api.openparser.solr.model.entity.MarkdownDocumentEntity;

public interface CustomMarkdownDocumentRepository {
    HighlightPage<MarkdownDocumentEntity> findDocuments(final String searchTerm, final Pageable page);
}
