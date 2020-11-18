package ru.gkomega.api.openparser.db.enumeration;

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
public enum DbErrorTemplateType implements ErrorTemplate {
    /**
     * common message templates
     */
    DB_WRITER_OPERATION_ERROR("error-0310", "error.db.writer.invalid"),
    DB_READER_OPERATION_ERROR("error-0320", "error.db.reader.invalid");

    /**
     * Default {@link String} error code
     */
    private final String code;
    /**
     * Default {@link String} error message
     */
    private final String message;

    /**
     * Returns {@link DbErrorTemplateType} by input {@link String} code
     *
     * @param value - initial input {@link String} code
     * @return {@link DbErrorTemplateType}
     */
    @Nullable
    public static DbErrorTemplateType findByCode(final String value) {
        return Arrays.stream(values())
            .filter(type -> type.getCode().equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns {@link List} of {@link DbErrorTemplateType}s
     *
     * @return {@link List} of {@link DbErrorTemplateType}s
     */
    @NonNull
    public static List<DbErrorTemplateType> buildErrorTemplateList() {
        return Collections.unmodifiableList(asList(DbErrorTemplateType.values()));
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
