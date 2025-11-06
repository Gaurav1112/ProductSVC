package org.responsive.productsvc.cache;

import org.responsive.productsvc.dto.ProductRecord;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Keeps an LRU per type. Capacity is distributed across types using a simple per-type slice
 * (capacity/10) but never less than 1. This is intentionally simple — it's deterministic and predictable.
 */
public class TypeBasedLRUCache implements ProductCache {

    private final int capacity;
    private final int perTypeCapacity;
    private final Map<String, LinkedHashMap<String, ProductRecord>> mapByType;
    private final Map<String, ProductRecord> indexById;

    public TypeBasedLRUCache(int capacity) {
        this.capacity = Math.max(1, capacity);
        this.perTypeCapacity = Math.max(1, this.capacity / 10); // simple distribution
        this.mapByType = new ConcurrentHashMap<>();
        this.indexById = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<ProductRecord> getById(String id) {
        return Optional.ofNullable(indexById.get(id));
    }

    @Override
    public List<ProductRecord> getByType(String type) {
        if (type == null) return Collections.emptyList();
        var lru = mapByType.get(type.toLowerCase());
        if (lru == null) return Collections.emptyList();
        synchronized (lru) {
            // return copy to avoid mutation by caller
            return new ArrayList<>(lru.values());
        }
    }

    @Override
    public void put(ProductRecord product) {
        if (product == null || product.id() == null) return;
        String id = product.id();
        String type = product.type() == null ? "" : product.type().toLowerCase();

        indexById.put(id, product);

        // put into type LRU
        var lru = mapByType.computeIfAbsent(type, k -> createLRU());
        synchronized (lru) {
            lru.put(id, product);
            if (lru.size() > perTypeCapacity) {
                Iterator<Map.Entry<String, ProductRecord>> it = lru.entrySet().iterator();
                if (it.hasNext()) {
                    Map.Entry<String, ProductRecord> eldest = it.next();
                    it.remove();
                    indexById.remove(eldest.getKey());
                }
            }
        }
    }

    @Override
    public void remove(String id) {
        if (id == null) return;
        var p = indexById.remove(id);
        if (p == null) return;
        String type = p.type() == null ? "" : p.type().toLowerCase();
        var lru = mapByType.get(type);
        if (lru != null) {
            synchronized (lru) {
                lru.remove(id);
            }
        }
    }

    @Override
    public void clear() {
        indexById.clear();
        mapByType.values().forEach(lru -> {
            synchronized (lru) { lru.clear(); }
        });
        mapByType.clear();
    }

    private LinkedHashMap<String, ProductRecord> createLRU() {
        return new LinkedHashMap<>(perTypeCapacity, 0.75f, true);
    }
}
