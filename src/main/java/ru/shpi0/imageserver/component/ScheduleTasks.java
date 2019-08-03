package ru.shpi0.imageserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.shpi0.imageserver.cache.Cache;
import ru.shpi0.imageserver.model.Avatar;
import ru.shpi0.imageserver.util.CacheFactory;

@Component
@EnableScheduling
public class ScheduleTasks {

    private final Cache<Avatar> cache;
    private static final int CACHE_LIFE_TIME = 10;

    @Autowired
    public ScheduleTasks(CacheFactory<Avatar> cacheFactory) {
        cache = cacheFactory.getCache();
    }

    @Scheduled(fixedDelay = CACHE_LIFE_TIME * 60 * 1000)
    private void cacheInvalidate() {
        cache.invalidate(CACHE_LIFE_TIME);
    }

}
