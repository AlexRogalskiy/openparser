package ru.gkomega.api.openparser.solr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gkomega.api.openparser.commons.model.OffsetPageRequest;
import ru.gkomega.api.openparser.solr.model.entity.MarkdownDocumentEntity;
import ru.gkomega.api.openparser.solr.model.dto.MarkdownDocumentDto;
import ru.gkomega.api.openparser.solr.repository.SolrMarkdownDocumentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class MarkdownDocumentController {

    private final SolrMarkdownDocumentRepository repository;

    @GetMapping
    public ResponseEntity<List<MarkdownDocumentDto>> getDocuments(@RequestParam final String searchTerm,
                                                                  @RequestParam(defaultValue = "0") final int offset,
                                                                  @RequestParam(defaultValue = "10") final int limit) {
        final HighlightPage<MarkdownDocumentEntity> page = this.repository.findDocuments(searchTerm, new OffsetPageRequest(offset, limit));
        return new ResponseEntity<>(page.stream()
            .map(document -> getResult(document, page.getHighlights(document)))
            .collect(Collectors.toList()), getHeaders(page), HttpStatus.OK);
    }

    private MarkdownDocumentDto getResult(final MarkdownDocumentEntity document, final List<HighlightEntry.Highlight> highlights) {
        final Map<String, List<String>> highlightMap = highlights.stream().collect(Collectors.toMap(h -> h.getField().getName(), HighlightEntry.Highlight::getSnipplets));
        return new MarkdownDocumentDto(document.getId(), document.getLastModified(), document.getContent(), document.getScore(), highlightMap);
    }

    private HttpHeaders getHeaders(final Page<?> page) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Elements", Long.toString(page.getTotalElements()));
        return headers;
    }
}
