package ru.shpi0.imageserver.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public abstract class AbstractRepository<T> implements CommonRepository<T> {

    private static Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(T t) {
        logger.debug("Saving entity {}", t);
        entityManager.persist(t);
    }

    @Override
    public void delete(T t) {
        logger.debug("Deleting entity {}", t);
        entityManager.remove(t);
    }

    @Override
    public void update(T t) {
        logger.debug("Updating entity {}", t);
        entityManager.persist(t);
    }

}
