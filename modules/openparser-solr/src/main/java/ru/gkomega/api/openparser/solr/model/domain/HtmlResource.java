package ru.gkomega.api.openparser.solr.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HtmlResource {
    private Resource resource;
    private String html;
}
