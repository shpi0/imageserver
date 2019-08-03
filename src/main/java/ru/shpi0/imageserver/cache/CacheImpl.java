package ru.shpi0.imageserver.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class CacheImpl<T> implements Cache<T>{

    private static Logger logger = LoggerFactory.getLogger(CacheImpl.class);

    int DEFAULT_CAPACITY = 100;

    int cacheCapacity;
    LinkedHashMap<Long, CacheEntry<T>> cache;

    @Override
    public void addToCache(Long id, T t) {
        logger.debug("Add to LFU cache with key {}", id);
        cache.put(id, new CacheEntry<>(t));
    }

    @Override
    public T getFromCache(Long id) {
        logger.debug("Get from LFU cache with key {}", id);
        CacheEntry<T> entry = cache.get(id);
        if (entry != null) {
            if (!entry.isInvalidate()) {
                return entry.getT();
            }
        }
        return null;
    }

    @Override
    public List<Long> getCacheState() {
        logger.debug("Get LFU cache state");
        List<Long> result = new ArrayList<>();
        for (Map.Entry<Long, CacheEntry<T>> entry :cache.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }

    @Override
    public int getCapacity() {
        return cacheCapacity;
    }

    @Override
    public void invalidate(Long key) {
        CacheEntry<T> cachedObject = cache.get(key);
        if (cachedObject != null) {
            cachedObject.setInvalidate(true);
        }
    }

    @Override
    public void invalidate(int period) {
        for (Map.Entry<Long, CacheEntry<T>> entry :cache.entrySet()) {
            if (entry.getValue().getCreated().plusMinutes(period).isBefore(LocalDateTime.now())) {
                invalidate(entry.getKey());
            }
        }
    }

}
