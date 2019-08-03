package ru.shpi0.imageserver.cache;

import java.time.LocalDateTime;

public class CacheEntry<T> implements Comparable<CacheEntry<T>> {

    private int usageCounter;

    private boolean invalidate;

    private LocalDateTime created;

    private T t;

    public CacheEntry(T t) {
        this.usageCounter = 0;
        this.invalidate = false;
        this.created = LocalDateTime.now();
        this.t = t;
    }

    public int getUsageCounter() {
        return usageCounter;
    }

    public void setUsageCounter(int usageCounter) {
        this.usageCounter = usageCounter;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public boolean isInvalidate() {
        return invalidate;
    }

    public void setInvalidate(boolean invalidate) {
        this.invalidate = invalidate;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public int compareTo(CacheEntry<T> o) {
        return Integer.compare(usageCounter, o.usageCounter);
    }
}
