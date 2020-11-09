package ru.gkomega.api.openparser.xml.configuration;

import org.modelmapper.ModelMapper;

/**
 * {@link ModelMapper} customizer declaration
 */
@FunctionalInterface
public interface ModelMapperCustomizer {
    /**
     * Customizes input {@link ModelMapper} instance
     *
     * @param modelMapper - initial input {@link ModelMapper} instance to customize
     */
    void customize(final ModelMapper modelMapper);
}
