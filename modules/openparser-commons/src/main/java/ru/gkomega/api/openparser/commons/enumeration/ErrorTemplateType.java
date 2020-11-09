package ru.gkomega.api.openparser.commons.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Getter
@RequiredArgsConstructor
public enum ErrorTemplateType implements ErrorTemplate {
    /**
     * common message templates
     */
    INVALID_DATA("error-0020", "error.data.invalid"),
    INVALID_CONFIGURATION("error-0030", "error.configuration.invalid");

    /**
     * Default {@link String} error code
     */
    private final String code;
    /**
     * Default {@link String} error message
     */
    private final String message;

    /**
     * Returns {@link ErrorTemplateType} by input {@link String} code
     *
     * @param value - initial input {@link String} code
     * @return {@link ErrorTemplateType}
     */
    @Nullable
    public static ErrorTemplateType findByCode(final String value) {
        return Arrays.stream(values())
            .filter(type -> type.getCode().equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns {@link List} of {@link ErrorTemplateType}s
     *
     * @return {@link List} of {@link ErrorTemplateType}s
     */
    @NonNull
    public static List<ErrorTemplateType> buildErrorTemplateList() {
        return Collections.unmodifiableList(asList(ErrorTemplateType.values()));
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
