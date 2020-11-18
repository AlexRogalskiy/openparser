package ru.gkomega.api.openparser.solr.property;

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

import javax.validation.constraints.NotBlank;

import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_DELIMITER;

@Data
@Validated
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = MarkdownSolrResourceProperty.PROPERTY_PREFIX, ignoreInvalidFields = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Markdown Solr Resource configuration properties")
public class MarkdownSolrResourceProperty extends ResourceProperty {
    /**
     * Default solr resource property prefix
     */
    public static final String PROPERTY_PREFIX = ResourceProperty.PROPERTY_PREFIX + DEFAULT_PROPERTY_DELIMITER + "markdown";

    /**
     * Extract path pattern
     */
    @NotBlank(message = "{property.batch.resources.xml.extract-path.notBlank}")
    private String extractPath;

    /**
     * Cron pattern
     */
    @NotBlank(message = "{property.batch.resources.xml.cron.notBlank}")
    private String cron;
}
