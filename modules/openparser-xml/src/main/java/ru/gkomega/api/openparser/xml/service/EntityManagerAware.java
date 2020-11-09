package ru.gkomega.api.openparser.xml.service;

import org.springframework.beans.factory.Aware;

import javax.persistence.EntityManager;

/**
 * Entity manager {@link Aware} interface declaration
 */
@FunctionalInterface
public interface EntityManagerAware extends Aware {
    /**
     * Initiates input {@link EntityManager}
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
     * or a custom init-method.
     *
     * @param entityManager - initial input {@link EntityManager} to be used within the applicationContext
     */
    void setEntityManager(final EntityManager entityManager);
}
