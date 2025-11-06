package org.responsive.productsvc.cache;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import org.responsive.productsvc.dto.ProductRecord;

import static org.junit.jupiter.api.Assertions.*;

class KeyBasedLRUCacheTest {

    @Test
    void evictionShouldRemoveOldestWhenCapacityExceeded() {
        int capacity = 3;
        KeyBasedLRUCache cache = new KeyBasedLRUCache(capacity);

        var p1 = new ProductRecord("P1","n","T","C",100,"18-45", Map.of());
        var p2 = new ProductRecord("P2","n","T","C",200,"18-45", Map.of());
        var p3 = new ProductRecord("P3","n","T","C",300,"18-45", Map.of());
        var p4 = new ProductRecord("P4","n","T","C",400,"18-45", Map.of());

        cache.put(p1);
        cache.put(p2);
        cache.put(p3);

        // Access p1 to make it most recently used
        Optional<ProductRecord> got = cache.getById("P1");
        assertTrue(got.isPresent());

        // Adding p4 should evict least recently used, which is p2 (p1 was just accessed)
        cache.put(p4);

        assertTrue(cache.getById("P1").isPresent());
        assertFalse(cache.getById("P2").isPresent());
        assertTrue(cache.getById("P3").isPresent());
        assertTrue(cache.getById("P4").isPresent());
    }
}

