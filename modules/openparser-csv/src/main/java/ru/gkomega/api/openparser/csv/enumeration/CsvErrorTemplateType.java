package ru.gkomega.api.openparser.csv.enumeration;

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
public enum CsvErrorTemplateType implements ErrorTemplate {
    /**
     * common message templates
     */
    CSV_WRITER_OPERATION_ERROR("error-0410", "error.csv.writer.invalid"),
    CSV_READER_OPERATION_ERROR("error-0420", "error.csv.reader.invalid");

    /**
     * Default {@link String} error code
     */
    private final String code;
    /**
     * Default {@link String} error message
     */
    private final String message;

    /**
     * Returns {@link CsvErrorTemplateType} by input {@link String} code
     *
     * @param value - initial input {@link String} code
     * @return {@link CsvErrorTemplateType}
     */
    @Nullable
    public static CsvErrorTemplateType findByCode(final String value) {
        return Arrays.stream(values())
            .filter(type -> type.getCode().equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns {@link List} of {@link CsvErrorTemplateType}s
     *
     * @return {@link List} of {@link CsvErrorTemplateType}s
     */
    @NonNull
    public static List<CsvErrorTemplateType> buildErrorTemplateList() {
        return Collections.unmodifiableList(asList(CsvErrorTemplateType.values()));
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
