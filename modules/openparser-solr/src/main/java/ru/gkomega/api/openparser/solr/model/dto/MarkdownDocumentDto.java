package ru.gkomega.api.openparser.solr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkdownDocumentDto {
    private String id;
    private LocalDateTime lastModified;
    private String content;
    private float score;
    private Map<String, List<String>> highlights;
}
