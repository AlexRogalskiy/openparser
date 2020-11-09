package ru.gkomega.api.openparser.xml.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.validation.annotation.Validated;
import ru.gkomega.api.openparser.batch.property.ResourceProperty;
import ru.gkomega.api.openparser.commons.annotation.NullOrNotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_DELIMITER;

@Data
@Validated
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = XmlResourceProperty.PROPERTY_PREFIX, ignoreInvalidFields = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Xml Batch Resource configuration properties")
public class XmlResourceProperty extends ResourceProperty {
    /**
     * Default xml resource property prefix
     */
    public static final String PROPERTY_PREFIX = ResourceProperty.PROPERTY_PREFIX + DEFAULT_PROPERTY_DELIMITER + "xml";

    /**
     * Root fragment element names
     */
    @Valid
    @NullOrNotEmpty(message = "{property.batch.resources.xml.root-elements.nullOrNotEmpty}")
    private List<@NotBlank String> rootElements;

    /**
     * Returns {@link String} array of root elements
     *
     * @return {@link String} array of root elements
     */
    public String[] getRootElements() {
        return Optional.ofNullable(this.rootElements)
                .orElseGet(Collections::emptyList)
                .toArray(new String[0]);
    }
}
