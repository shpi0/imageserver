package ru.shpi0.imageserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.shpi0.imageserver.cache.Cache;

@Component
public class CacheFactory<T> {

    private static Logger logger = LoggerFactory.getLogger(CacheFactory.class);

    private Cache<T> lfuCache;
    private Cache<T> lruCache;
    private String cacheImpl;

    @Autowired
    public CacheFactory(@Qualifier("LfuCacheImpl") Cache<T> lfuCache, @Qualifier("LruCacheImpl") Cache<T> lruCache,
                        Environment environment) {
        this.lfuCache = lfuCache;
        this.lruCache = lruCache;
        cacheImpl = environment.getProperty("data.cache.type", "LRU");
    }

    public Cache<T> getCache() {
        logger.debug("getCache() returns {}", cacheImpl);
        return "LRU".equals(cacheImpl) ? lruCache : lfuCache;
    }

}
