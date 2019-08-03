package ru.shpi0.imageserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shpi0.imageserver.cache.Cache;
import ru.shpi0.imageserver.model.Avatar;
import ru.shpi0.imageserver.repository.AvatarRepository;
import ru.shpi0.imageserver.util.CacheFactory;

@Service
public class AvatarServiceImpl implements AvatarService {

    private static Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final AvatarRepository avatarRepository;
    private final Cache<Avatar> cache;

    @Autowired
    public AvatarServiceImpl(AvatarRepository avatarRepository, CacheFactory<Avatar> cacheFactory) {
        this.avatarRepository = avatarRepository;
        this.cache = cacheFactory.getCache();
    }

    @Override
    public void save(Avatar avatar) {
        logger.debug("Save avatar {}", avatar);
        if (avatar.getId() != 0) {
            invalidateCache(avatar.getId());
        }
        avatarRepository.save(avatar);
    }

    @Override
    public Avatar getById(long id) {
        logger.debug("Trying to get avatar by id {}", id);
        Avatar result = cache.getFromCache(id);
        if (result != null) {
            logger.debug("Got avatar from cache by id {}", id);
            return result;
        }
        logger.debug("No avatar in cache with id {}, trying to get from DB", id);
        result = avatarRepository.getById(id);
        cache.addToCache(id, result);
        logger.debug("Got avatar from DB by id {} and stored in cache", id);
        return result;
    }

    @Override
    public Long getCount() {
        logger.debug("Got count of Avatar entities");
        return avatarRepository.getCount();
    }

    private void invalidateCache(long id) {
        cache.invalidate(id);
    }

}
