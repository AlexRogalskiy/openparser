package ru.gkomega.api.openparser.commons.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static ru.gkomega.api.openparser.commons.model.ExceptionDto.createException;

/**
 * Exception model configurer interface declaration
 */
public interface ExceptionModelConfigurer {

    /**
     * Exception entry model declaration
     */
    interface ExceptionModel extends Serializable {
        /**
         * Returns {@link String} request path
         *
         * @return {@link String} request path
         */
        String getPath();

        /**
         * Returns {@link String} response status
         *
         * @return {@link String} response status
         */
        Integer getStatus();

        /**
         * Returns {@link String} response error
         *
         * @return {@link String} response error
         */
        String getError();

        /**
         * Returns {@link String} error message
         *
         * @return {@link String} error message
         */
        String getMessage();

        /**
         * Returns {@link Date} response timestamp
         *
         * @return {@link Date} response timestamp
         */
        Date getTimestamp();
    }

    /**
     * Returns {@link ResponseEntity} with error informative parameters
     *
     * @param path    initial input {@link String} path
     * @param message initial input {@link String} message
     * @param status  initial input {@link HttpStatus} status
     * @return {@link ResponseEntity} instance
     */
    @NotNull
    static ResponseEntity<Object> buildResponseEntity(final String path,
                                                      final String message,
                                                      final HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(createException(path, message, status));
    }
}
