package ru.gkomega.api.openparser.xstream.configuration;

import com.thoughtworks.xstream.XStream;

/**
 * {@link XStream} customizer declaration
 */
@FunctionalInterface
public interface XStreamCustomizer {
    /**
     * Customizes input {@link XStream}
     *
     * @param xstream - initial input {@link XStream} to customize
     */
    void customize(final XStream xstream);
}
