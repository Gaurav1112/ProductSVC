package org.responsive.productsvc.cache;

import java.util.*;
import java.util.stream.Collectors;
import org.responsive.productsvc.dto.ProductRecord;

public class KeyBasedLRUCache implements ProductCache {
    private final int capacity;
    private final Map<String, ProductRecord> map;

    public KeyBasedLRUCache(int capacity) {
        this.capacity = capacity;
        // accessOrder = true for LRU
        this.map = Collections.synchronizedMap(new LinkedHashMap<>(capacity, 0.75f, true) {
            private static final long serialVersionUID = 1L;
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, ProductRecord> eldest) {
                return size() > KeyBasedLRUCache.this.capacity;
            }
        });
    }

    @Override
    public Optional<ProductRecord> getById(String id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<ProductRecord> getByType(String type) {
        synchronized (map) {
            return map.values()
                      .stream()
                      .filter(p -> p.type() != null && p.type().equals(type))
                      .collect(Collectors.toList());
        }
    }

    @Override
    public void put(ProductRecord product) {
        if (product == null || product.id() == null) return;
        map.put(product.id(), product);
    }

    @Override
    public void remove(String id) {
        map.remove(id);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
