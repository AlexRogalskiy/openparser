package ru.gkomega.api.openparser.xml.service;

import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Common DAO service implementation
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class EntityDaoService implements EntityManagerAware {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public <ID, T extends Persistable<ID>> T get(final Class<T> type, final ID id) {
        return this.entityManager.find(type, id);
    }

    public <ID, T extends Persistable<ID>> T update(final T entity) {
        return this.entityManager.merge(entity);
    }

    public <ID, T extends Persistable<ID>> void save(final T entity) {
        this.entityManager.persist(entity);
    }

    public <ID, T extends Persistable<ID>> void delete(final T entity) {
        this.entityManager.remove(entity);
    }

    @SuppressWarnings("rawtypes")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public <ID, T extends Persistable<ID>> List getAll(final Class<? extends T> tableClass) {
        final Query query = this.entityManager.createQuery("from " + tableClass.getSimpleName());
        return query.getResultList();
    }

    @Override
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
