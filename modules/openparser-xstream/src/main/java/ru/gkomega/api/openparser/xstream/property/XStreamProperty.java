package ru.gkomega.api.openparser.xstream.property;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;
import ru.gkomega.api.openparser.commons.annotation.NullOrNotBlank;
import ru.gkomega.api.openparser.commons.annotation.NullOrNotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Map;

import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_DELIMITER;
import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_PREFIX;

@Data
@Validated
@Accessors(chain = true)
@ConfigurationProperties(prefix = XStreamProperty.PROPERTY_PREFIX, ignoreInvalidFields = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Xstream configuration properties")
public class XStreamProperty {
    /**
     * Default xstream property prefix
     */
    public static final String PROPERTY_PREFIX = DEFAULT_PROPERTY_PREFIX + DEFAULT_PROPERTY_DELIMITER + "xstream";

    /**
     * Xstream configuration settings
     */
    @Valid
    @NullOrNotEmpty(message = "{property.xstream.settings.nullOrNotEmpty}")
    private Map<@NotBlank String, @NotNull XStreamConfiguration> settings;

    /**
     * XStream configuration
     */
    @Data
    @Validated
    @Accessors(chain = true)
    public static class XStreamConfiguration {
        /**
         * Ignore unknown elements pattern
         */
        @NullOrNotBlank(message = "{property.xstream.settings.ignore-pattern.nullOrNotBlank}")
        private String ignorePattern = ".*";

        /**
         * Batch mode
         */
        @NumberFormat(style = NumberFormat.Style.NUMBER)
        @Positive(message = "{property.xstream.settings.mode.positive}")
        private int mode = XStream.NO_REFERENCES;

        /**
         * Autodetect annotations
         */
        private boolean autodetectAnnotations = true;

        /**
         * Stream driver
         * * {@link StaxDriver}
         * * {@link XppDriver}
         * * {@link JettisonMappedXmlDriver}
         * * {@link JsonHierarchicalStreamDriver}
         */
        private HierarchicalStreamDriver streamDriver = new StaxDriver();

        /**
         * Enable/disable xstream settings configuration ({@code true} by default)
         */
        private boolean enabled = true;
    }

    /**
     * Enable/disable xstream configuration ({@code true} by default)
     */
    private boolean enabled = true;
}
