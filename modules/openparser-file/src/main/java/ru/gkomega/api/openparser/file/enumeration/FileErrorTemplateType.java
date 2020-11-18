package ru.gkomega.api.openparser.file.enumeration;

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
public enum FileErrorTemplateType implements ErrorTemplate {
    /**
     * common message templates
     */
    FILE_WRITER_OPERATION_ERROR("error-0110", "error.file.writer.invalid"),
    FILE_READER_OPERATION_ERROR("error-0120", "error.file.reader.invalid");

    /**
     * Default {@link String} error code
     */
    private final String code;
    /**
     * Default {@link String} error message
     */
    private final String message;

    /**
     * Returns {@link FileErrorTemplateType} by input {@link String} code
     *
     * @param value - initial input {@link String} code
     * @return {@link FileErrorTemplateType}
     */
    @Nullable
    public static FileErrorTemplateType findByCode(final String value) {
        return Arrays.stream(values())
            .filter(type -> type.getCode().equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns {@link List} of {@link FileErrorTemplateType}s
     *
     * @return {@link List} of {@link FileErrorTemplateType}s
     */
    @NonNull
    public static List<FileErrorTemplateType> buildErrorTemplateList() {
        return Collections.unmodifiableList(asList(FileErrorTemplateType.values()));
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
