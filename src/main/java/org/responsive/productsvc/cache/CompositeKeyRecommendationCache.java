package org.responsive.productsvc.cache;

import org.responsive.productsvc.dto.ProductRecord;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple thread-safe LRU cache for recommendation results keyed by composite query params.
 */
public class CompositeKeyRecommendationCache implements RecommendationCache {

    private final int capacity;
    private final Map<String, List<ProductRecord>> map;
    private final Object lock = new Object();

    public CompositeKeyRecommendationCache(int capacity) {
        this.capacity = Math.max(1, capacity);
        // LinkedHashMap with access-order inside synchronized wrapper
        this.map = Collections.synchronizedMap(new LinkedHashMap<>(capacity, 0.75f, true) {
            private static final long serialVersionUID = 1L;
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<ProductRecord>> eldest) {
                return size() > CompositeKeyRecommendationCache.this.capacity;
            }
        });
    }

    @Override
    public Optional<List<ProductRecord>> get(String key) {
        if (key == null) return Optional.empty();
        List<ProductRecord> v;
        synchronized (lock) {
            v = map.get(key);
            if (v == null) return Optional.empty();
            // return a defensive copy
            return Optional.of(Collections.unmodifiableList(new ArrayList<>(v)));
        }
    }

    @Override
    public void put(String key, List<ProductRecord> value) {
        if (key == null || value == null) return;
        synchronized (lock) {
            map.put(key, new ArrayList<>(value));
        }
    }

    @Override
    public void remove(String key) {
        if (key == null) return;
        synchronized (lock) {
            map.remove(key);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            map.clear();
        }
    }
}
