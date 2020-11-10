package ru.gkomega.api.openparser.csv.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gkomega.api.openparser.xstream.converter.UuidConverter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("entry")
public class CatalogItemDto implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 7101122785102747454L;

    @XStreamAlias("id")
    @XStreamConverter(UuidConverter.class)
    private UUID refKey;

    @XStreamAlias("category")
    private String category;

    @XStreamAlias("title")
    private String title;

    @XStreamAlias("updated")
    private LocalDateTime updatedAt;

    @XStreamAlias("author")
    private String author;

    @XStreamAlias("summary")
    private String summary;

    @XStreamOmitField
    @XStreamAlias("link")
    private Object link;
}
