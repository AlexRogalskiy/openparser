package ru.gkomega.api.openparser.batch.property;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_DELIMITER;
import static ru.gkomega.api.openparser.commons.configuration.PropertySettings.DEFAULT_PROPERTY_PREFIX;

@Data
@Validated
@Accessors(chain = true)
@ConfigurationProperties(prefix = BatchProperty.PROPERTY_PREFIX, ignoreInvalidFields = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Batch configuration properties")
public class BatchProperty {
    /**
     * Default batch property prefix
     */
    public static final String PROPERTY_PREFIX = DEFAULT_PROPERTY_PREFIX + DEFAULT_PROPERTY_DELIMITER + "batch";

    /**
     * Configures the automatic registration of job configurations.
     */
    @NotNull(message = "{property.batch.config.notNull}")
    private JobConfigurationProperty config = new JobConfigurationProperty();

    /**
     * Batch chunk size
     */
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Positive(message = "{property.batch.chunk-size.positive}")
    private int chunkSize = 10;

    @Data
    @Validated
    @Accessors(chain = true)
    public static class JobConfigurationProperty {
        /**
         * Location in the classpath where Spring Batch job definitions in XML are picked up.
         */
        @NotBlank(message = "{property.batch.path-xml.notBlank}")
        private String pathXml = "classpath*:/META-INF/spring/batch/jobs/*.xml";

        /**
         * Package where Spring Batch job definitions in JavaConfig are picked up.
         */
        @NotBlank(message = "{property.batch.base-package.notBlank}")
        private String packageJavaConfig = "spring.batch.jobs";
    }

    /**
     * Enable/disable batch configuration ({@code true} by default)
     */
    private boolean enabled = true;
}
