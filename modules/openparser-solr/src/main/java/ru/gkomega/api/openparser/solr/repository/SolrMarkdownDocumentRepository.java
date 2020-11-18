package ru.gkomega.api.openparser.solr.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import ru.gkomega.api.openparser.solr.model.entity.MarkdownDocumentEntity;

@Repository
public interface SolrMarkdownDocumentRepository extends SolrCrudRepository<MarkdownDocumentEntity, String>, CustomMarkdownDocumentRepository {
}
