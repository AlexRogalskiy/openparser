package ru.gkomega.api.openparser.batch.configuration;

import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import javax.annotation.PostConstruct;

/**
 * Extend this class to add custom {@link ApplicationContextFactory}.
 */
public abstract class AutomaticJobRegistrarConfigurationSupport {

    @Autowired
    private AutomaticJobRegistrar automaticJobRegistrar;

    @PostConstruct
    private void initialize() throws Exception {
        // Default order for the AutomaticJobRegistrar is Ordered.LOWEST_PRECEDENCE. Since we want to register
        // listeners after the jobs are registered through the AutomaticJobRegistrar, we need to decrement its
        // order value by one. The creation of the AutomaticJobRegistrar bean is hidden deep in the automatic
        // batch configuration, so we unfortunately have to do it here.
        this.automaticJobRegistrar.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        this.addApplicationContextFactories(this.automaticJobRegistrar);
    }

    /**
     * Add ApplicationContextFactories to the given job registrar.
     *
     * @param automaticJobRegistrar Bean
     * @throws Exception Some error.
     */
    protected abstract void addApplicationContextFactories(final AutomaticJobRegistrar automaticJobRegistrar) throws Exception;
}
