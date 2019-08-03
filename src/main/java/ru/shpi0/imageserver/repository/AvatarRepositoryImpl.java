package ru.shpi0.imageserver.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.shpi0.imageserver.model.Avatar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

@Repository
public class AvatarRepositoryImpl extends AbstractRepository<Avatar> implements AvatarRepository {

    private static Logger logger = LoggerFactory.getLogger(AvatarRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Avatar getById(long id) {
        logger.debug("Getting avatar by id {}", id);
        try {
            return entityManager.createQuery("SELECT a FROM Avatar a WHERE a.id = :id", Avatar.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            logger.error("Exception during getting avatar by id {}", id, e);
        }
        return null;
    }

    @Override
    public Long getCount() {
        logger.debug("Getting count of avatars in DB");
        try {
            return ((BigInteger) entityManager
                    .createNativeQuery("SELECT COUNT(*) FROM avatar WHERE 1;")
                    .getSingleResult()).longValue();
        } catch (Exception ignore) {

        }
        return 0L;
    }

}
