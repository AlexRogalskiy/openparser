package ru.gkomega.api.openparser.csv.property;

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

import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_DELIMITER;

@Data
@Validated
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = CsvResourceProperty.PROPERTY_PREFIX, ignoreInvalidFields = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Csv Batch Resource configuration properties")
public class CsvResourceProperty extends ResourceProperty {
    /**
     * Default csv resource property prefix
     */
    public static final String PROPERTY_PREFIX = ResourceProperty.PROPERTY_PREFIX + DEFAULT_PROPERTY_DELIMITER + "csv";
}
