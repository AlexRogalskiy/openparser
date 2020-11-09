package ru.gkomega.api.openparser.commons.enumeration;

/**
 * Default error message info interface declaration
 */
public interface ErrorTemplate {
    /**
     * Returns {@link String} message template code
     *
     * @return {@link String} message template code
     */
    String getCode();

    /**
     * Returns {@link String} error message
     *
     * @return {@link String} error message
     */
    String getMessage();

    /**
     * Returns {@link String} error id
     *
     * @return {@link String} error id
     */
    default String id() {
        return this.getMessage();
    }

    /**
     * Returns {@link String} error name
     *
     * @return {@link String} error name
     */
    default String name() {
        return this.getCode();
    }
}
