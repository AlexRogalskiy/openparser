package ru.gkomega.api.openparser.commons.configuration;

import ru.gkomega.api.openparser.commons.exception.ConfigurationException;

import java.util.Optional;
import java.util.function.Function;

import static ru.gkomega.api.openparser.commons.exception.ConfigurationException.throwInvalidConfiguration;

/**
 * Provides configuration information by name
 *
 * @param <T> type of provided configuration
 */
@FunctionalInterface
public interface ConfigurationProvider<T> extends Function<String, T> {
    /**
     * Returns {@link T} configuration by input {@link String} endpoint name or throw {@link ConfigurationException}
     *
     * @param value initial input {@link String} endpoint name to fetch by
     * @return {@link T} configuration
     * @throws ConfigurationException if endpoint configuration is not available
     */
    default T getOrThrow(final String value) {
        try {
            return Optional.ofNullable(this.apply(value))
                .orElseThrow(() -> throwInvalidConfiguration(value));
        } catch (Throwable ex) {
            throw new ConfigurationException(ex);
        }
    }
}
