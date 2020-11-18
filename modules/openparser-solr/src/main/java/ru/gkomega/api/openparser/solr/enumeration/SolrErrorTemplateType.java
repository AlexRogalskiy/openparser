package ru.gkomega.api.openparser.solr.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.enumeration.ErrorTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Getter
@RequiredArgsConstructor
public enum SolrErrorTemplateType implements ErrorTemplate {
    /**
     * common message templates
     */
    SOLR_WRITER_OPERATION_ERROR("error-0510", "error.solr.writer.invalid"),
    SOLR_READER_OPERATION_ERROR("error-0520", "error.solr.reader.invalid");

    /**
     * Default {@link String} error code
     */
    private final String code;
    /**
     * Default {@link String} error message
     */
    private final String message;

    /**
     * Returns {@link SolrErrorTemplateType} by input {@link String} code
     *
     * @param value - initial input {@link String} code
     * @return {@link SolrErrorTemplateType}
     */
    @Nullable
    public static SolrErrorTemplateType findByCode(final String value) {
        return Arrays.stream(values())
            .filter(type -> type.getCode().equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns {@link List} of {@link SolrErrorTemplateType}s
     *
     * @return {@link List} of {@link SolrErrorTemplateType}s
     */
    @NonNull
    public static List<SolrErrorTemplateType> buildErrorTemplateList() {
        return Collections.unmodifiableList(asList(SolrErrorTemplateType.values()));
    }

    /**
     * Returns {@link String} representation of message code
     *
     * @return {@link String} representation of message code
     */
    @Override
    public String toString() {
        return String.format("Code: {%s}, message: {%s}", this.code, this.message);
    }
}
