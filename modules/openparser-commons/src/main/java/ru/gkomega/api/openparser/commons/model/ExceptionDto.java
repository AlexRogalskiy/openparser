package ru.gkomega.api.openparser.commons.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import ru.gkomega.api.openparser.commons.configuration.ExceptionModelConfigurer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Data
@Builder
@Validated
@AllArgsConstructor
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
        ExceptionDto.FieldNames.TIMESTAMP,
        ExceptionDto.FieldNames.STATUS,
        ExceptionDto.FieldNames.ERROR,
        ExceptionDto.FieldNames.MESSAGE,
        ExceptionDto.FieldNames.PATH
})
public class ExceptionDto implements ExceptionModelConfigurer.ExceptionModel {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 9038013069550984066L;

    @JsonProperty(value = FieldNames.PATH, required = true)
    @NotBlank(message = "{model.dto.exception-entry.path.notBlank}")
    private final String path;

    @JsonProperty(value = FieldNames.STATUS, required = true)
    @PositiveOrZero(message = "{model.dto.exception-entry.status.positiveOrZero}")
    private final Integer status;

    @JsonProperty(value = FieldNames.ERROR, required = true)
    @NotBlank(message = "{model.dto.exception-entry.error.notBlank}")
    private final String error;

    @JsonProperty(value = FieldNames.MESSAGE, required = true)
    @NotBlank(message = "{model.dto.exception-entry.message.notBlank}")
    private final String message;

    @JsonProperty(value = FieldNames.TIMESTAMP, required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_US")
    @NotNull(message = "{model.dto.exception-entry.timestamp.notNull}")
    private final Date timestamp;

    /**
     * Returns {@link ExceptionDto} by input parameters
     *
     * @param path    - initial input entry path {@link String}
     * @param message - initial input entry description {@link String}
     * @param status  - initial input entry status {@link HttpStatus}
     * @return {@link ExceptionDto}
     */
    @NonNull
    public static ExceptionDto createException(final String path,
                                               final String message,
                                               final HttpStatus status) {
        return ExceptionDto.builder()
                .path(path)
                .message(message)
                .error(status.getReasonPhrase())
                .status(status.value())
                .timestamp(new Date())
                .build();
    }
}
