package ru.shpi0.imageserver.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component("LfuCacheImpl")
public class LfuCacheImpl<T> extends CacheImpl<T> {

    private static Logger logger = LoggerFactory.getLogger(LfuCacheImpl.class);

    @Autowired
    public LfuCacheImpl(Environment environment) {
        this.cacheCapacity = DEFAULT_CAPACITY;
        try {
            this.cacheCapacity = Integer.parseInt(environment.getProperty("data.cache.size"));
        } catch (Exception ignore) {

        }
        initCache();
    }

    private void initCache() {
        logger.info("Initializing LFU cache");
        this.cache = new LinkedHashMap<Long, CacheEntry<T>>() {
            @Override
            public CacheEntry<T> put(Long key, CacheEntry<T> value) {
                if (size() >= cacheCapacity) {
                    int minCounter = Integer.MAX_VALUE;
                    Long minimalKey = null;
                    for (Map.Entry<Long, CacheEntry<T>> entry :cache.entrySet()) {
                        if (entry.getValue().getUsageCounter() < minCounter) {
                            minCounter = entry.getValue().getUsageCounter();
                            minimalKey = entry.getKey();
                        }
                    }
                    if (minimalKey != null) {
                        remove(minimalKey);
                    }
                }
                return super.put(key, value);
            }

            @Override
            public CacheEntry<T> get(Object key) {
                CacheEntry<T> result = super.get(key);
                if (result != null) {
                    result.setUsageCounter(result.getUsageCounter() + 1);
                }
                return result;
            }
        };
    }

}
