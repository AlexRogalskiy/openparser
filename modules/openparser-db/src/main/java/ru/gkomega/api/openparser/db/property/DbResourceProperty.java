package ru.gkomega.api.openparser.db.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ClassPathResource;
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
@ConfigurationProperties(prefix = DbResourceProperty.PROPERTY_PREFIX, ignoreInvalidFields = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Database Batch Resource configuration properties")
public class DbResourceProperty extends ResourceProperty {
    /**
     * Default database resource property prefix
     */
    public static final String PROPERTY_PREFIX = ResourceProperty.PROPERTY_PREFIX + DEFAULT_PROPERTY_DELIMITER + "db";

    /**
     * Database initial scripts
     */
    @Valid
    @NullOrNotEmpty(message = "{property.batch.resources.db.scripts.nullOrNotEmpty}")
    private List<@NotBlank String> scripts;

    /**
     * Returns {@link String} array initial scripts
     *
     * @return {@link String} array initial scripts
     */
    public ClassPathResource[] getScripts() {
        return Optional.ofNullable(this.scripts)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(ClassPathResource::new)
            .toArray(ClassPathResource[]::new);
    }
}
