package ru.gkomega.api.openparser.xstream.configuration;

import ru.gkomega.api.openparser.commons.configuration.ConfigurationProvider;
import ru.gkomega.api.openparser.xstream.property.XStreamProperty;

/**
 * Provides configuration information on web service endpoint by name
 */
@FunctionalInterface
public interface XStreamConfigurationProvider extends ConfigurationProvider<XStreamProperty.XStreamConfiguration> {
}
