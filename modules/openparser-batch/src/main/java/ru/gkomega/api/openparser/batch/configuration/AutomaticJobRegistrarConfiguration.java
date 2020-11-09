package ru.gkomega.api.openparser.batch.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.ModularBatchConfiguration;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import ru.gkomega.api.openparser.batch.property.BatchProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Configuration for registration of {@link ApplicationContextFactory} with the {@link AutomaticJobRegistrar} that is
 * instantiated inside the {@link ModularBatchConfiguration}.
 * <p>
 * This configuration looks for jobs in a modular fashion, meaning that every job configuration file gets its own
 * Child-ApplicationContext. Configuration files can be XML files in the location /META-INF/spring/batch/jobs,
 * overridable via property batch.path-xml, and JavaConfig classes in the package spring.batch.jobs, overridable
 * via property batch.base-package.
 * <p>
 * Customization is done by adding a Configuration class that extends {@link AutomaticJobRegistrarConfigurationSupport}.
 * This will disable this auto configuration.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnMissingBean(AutomaticJobRegistrarConfigurationSupport.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Batch Job registration configuration")
public abstract class AutomaticJobRegistrarConfiguration extends AutomaticJobRegistrarConfigurationSupport {

    private final BatchProperty batchProperty;

    /**
     * @see AutomaticJobRegistrarConfigurationSupport#addApplicationContextFactories(AutomaticJobRegistrar)
     */
    @Override
    protected void addApplicationContextFactories(final AutomaticJobRegistrar automaticJobRegistrar) throws Exception {
        this.registerJobsFromXml(automaticJobRegistrar);
        this.registerJobsFromJavaConfig(automaticJobRegistrar);
    }

    protected void registerJobsFromXml(AutomaticJobRegistrar automaticJobRegistrar) throws IOException {
        // Add all XML-Configurations to the AutomaticJobRegistrar
        final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        final Resource[] xmlConfigurations = resourcePatternResolver.getResources(this.batchProperty.getConfig().getPathXml());
        for (final Resource resource : xmlConfigurations) {
            log.info("Register jobs from {}", resource);
            automaticJobRegistrar.addApplicationContextFactory(new GenericApplicationContextFactory(resource));
        }
    }

    protected void registerJobsFromJavaConfig(final AutomaticJobRegistrar automaticJobRegistrar) throws ClassNotFoundException, IOException {
        final List<Class<?>> classes = this.findMyTypes(this.batchProperty.getConfig().getPackageJavaConfig());
        for (final Class<?> clazz : classes) {
            log.info("Register jobs from {}", clazz);
            automaticJobRegistrar.addApplicationContextFactory(new GenericApplicationContextFactory(clazz));
        }
    }

    private List<Class<?>> findMyTypes(final String basePackage) throws IOException, ClassNotFoundException {
        final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        final List<Class<?>> candidates = new ArrayList<>();
        final String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + this.resolveBasePackage(basePackage) + "/**/*.class";
        final Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (final Resource resource : resources) {
            if (resource.isReadable()) {
                final MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (this.isCandidate(metadataReader)) {
                    candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return candidates;
    }

    private String resolveBasePackage(final String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    private boolean isCandidate(final MetadataReader metadataReader) {
        try {
            final Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
            return Objects.nonNull(c.getAnnotation(Configuration.class));
        } catch (Throwable e) {
            return false;
        }
    }
}
