package com.ontherunvaro.evernoteclient.util;

import java.util.LinkedHashMap;

/**
 * Created by ontherunvaro on 1/08/16.
 */
public class SizeLimitedHashMap<K, V> extends LinkedHashMap<K, V> {

    private int maxSize;

    public SizeLimitedHashMap(int initialCapacity, int maxSize) {
        super(initialCapacity);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
