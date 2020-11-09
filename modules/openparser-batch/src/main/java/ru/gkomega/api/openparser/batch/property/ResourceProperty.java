package ru.gkomega.api.openparser.batch.property;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_DELIMITER;

@Data
@Validated
@Accessors(chain = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Batch Resource configuration properties")
public abstract class ResourceProperty {
    /**
     * Default resource property prefix
     */
    public static final String PROPERTY_PREFIX = BatchProperty.PROPERTY_PREFIX + DEFAULT_PROPERTY_DELIMITER + "resources";

    /**
     * Resource path pattern
     */
    @NotBlank(message = "{property.batch.resources.path-pattern.notBlank}")
    private String pathPattern = "classpath*:data/template.xml";
}
