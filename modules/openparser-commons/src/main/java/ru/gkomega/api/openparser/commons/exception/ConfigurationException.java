package ru.gkomega.api.openparser.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.commons.enumeration.ErrorTemplateType.INVALID_CONFIGURATION;

/**
 * Configuration {@link RuntimeException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConfigurationException extends RuntimeException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 5120429116335637195L;

    /**
     * Initializes {@link ConfigurationException} using the given {@code String} errorMessage
     *
     * @param message The message describing the exception
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Initializes {@link ConfigurationException} using the given {@code Throwable} type
     *
     * @param type - initial input {@link Throwable}
     */
    public ConfigurationException(final Throwable type) {
        super(type);
    }

    /**
     * Invalid mapping exception constructor with initial input {@link Throwable}
     *
     * @param message - initial input {@link String} message
     * @param type    - initial input {@link Throwable} type
     */
    public ConfigurationException(final String message, final Throwable type) {
        super(message, type);
    }

    /**
     * Returns {@link ConfigurationException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link ConfigurationException}
     */
    @NonNull
    public static ConfigurationException throwError(final String message) {
        throw new ConfigurationException(message);
    }

    /**
     * Returns {@link ConfigurationException} by input parameters
     *
     * @param target - initial input value {@link Object}
     * @return {@link ConfigurationException}
     */
    @NonNull
    public static ConfigurationException throwInvalidConfiguration(final Object target) {
        throw throwInvalidConfigurationWith(INVALID_CONFIGURATION.getCode(), target);
    }

    /**
     * Returns {@link ConfigurationException} by input parameters
     *
     * @param target    - initial input value {@link Object}
     * @param throwable - initial input error {@link Throwable}
     * @return {@link ConfigurationException}
     */
    @NonNull
    public static ConfigurationException throwInvalidConfiguration(final Object target,
                                                                   final Throwable throwable) {
        final String message = MessageSourceHelper.getMessage(INVALID_CONFIGURATION.getCode(), target);
        throw new ConfigurationException(message, throwable);
    }

    /**
     * Returns {@link ConfigurationException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link ConfigurationException}
     */
    @NonNull
    public static ConfigurationException throwInvalidConfigurationWith(final String messageId,
                                                                       @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
