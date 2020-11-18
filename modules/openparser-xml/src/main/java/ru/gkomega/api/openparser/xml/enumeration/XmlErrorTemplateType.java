package ru.gkomega.api.openparser.xml.enumeration;

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
public enum XmlErrorTemplateType implements ErrorTemplate {
    /**
     * common message templates
     */
    XML_WRITER_OPERATION_ERROR("error-0210", "error.xml.writer.invalid"),
    XML_READER_OPERATION_ERROR("error-0220", "error.xml.reader.invalid");

    /**
     * Default {@link String} error code
     */
    private final String code;
    /**
     * Default {@link String} error message
     */
    private final String message;

    /**
     * Returns {@link XmlErrorTemplateType} by input {@link String} code
     *
     * @param value - initial input {@link String} code
     * @return {@link XmlErrorTemplateType}
     */
    @Nullable
    public static XmlErrorTemplateType findByCode(final String value) {
        return Arrays.stream(values())
            .filter(type -> type.getCode().equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns {@link List} of {@link XmlErrorTemplateType}s
     *
     * @return {@link List} of {@link XmlErrorTemplateType}s
     */
    @NonNull
    public static List<XmlErrorTemplateType> buildErrorTemplateList() {
        return Collections.unmodifiableList(asList(XmlErrorTemplateType.values()));
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
