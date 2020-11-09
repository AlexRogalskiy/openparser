package ru.gkomega.api.openparser.xml.model.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("note")
public class NoteDto implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 5039612189906269062L;

    /**
     * Title element
     */
    @XStreamAlias("title")
    private String title;
    /**
     * Description element
     */
    @XStreamAlias("description")
    private String description;
}
