package ru.shpi0.imageserver.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component("LruCacheImpl")
public class LruCacheImpl<T> extends CacheImpl<T> {

    private static Logger logger = LoggerFactory.getLogger(LruCacheImpl.class);

    @Autowired
    public LruCacheImpl(Environment environment) {
        this.cacheCapacity = DEFAULT_CAPACITY;
        try {
            this.cacheCapacity = Integer.parseInt(environment.getProperty("data.cache.size"));
        } catch (Exception ignore) {

        }
        initCache();
    }

    private void initCache() {
        logger.info("Initializing LRU cache");
        this.cache = new LinkedHashMap<Long, CacheEntry<T>>(cacheCapacity, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > cacheCapacity;
            }
        };
    }

}
