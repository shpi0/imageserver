package ru.shpi0.imageserver.cache;

import java.util.List;

public interface Cache<T> {

    void addToCache(Long key, T t);

    T getFromCache(Long key);

    List<Long> getCacheState();

    int getCapacity();

    void invalidate(Long key);

    void invalidate(int period);

}
