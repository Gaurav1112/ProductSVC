package org.responsive.productsvc.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.responsive.productsvc.cache.ProductCache;
import org.responsive.productsvc.dto.ProductRecord;
import org.responsive.productsvc.repository.ProductDocument;
import org.responsive.productsvc.repository.ProductRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductRepository repository;
    private ProductCache cache;
    private ProductServiceImpl service;

    @BeforeEach
    void setup() {
        repository = mock(ProductRepository.class);
        cache = mock(ProductCache.class);
        service = new ProductServiceImpl(repository, cache);
    }

    @Test
    void shouldFetchFromCacheIfAvailable() {
        ProductRecord cached = new ProductRecord("p1", "Toy Car", "Toy", "Kids", 500, "3-6", Map.of());
        when(cache.getById("p1")).thenReturn(Optional.of(cached));

        Optional<ProductRecord> result = service.getById("p1");

        assertThat(result).isPresent();
        verify(repository, never()).findById("p1");
    }

    @Test
    void shouldFetchFromRepositoryIfNotInCache() {
        ProductDocument doc = new ProductDocument("p2", "Doll", "Toy", "Kids", 300, "3-6", Map.of());
        when(cache.getById("p2")).thenReturn(Optional.empty());
        when(repository.findById("p2")).thenReturn(Optional.of(doc));

        Optional<ProductRecord> result = service.getById("p2");

        assertThat(result).isPresent();
        verify(repository).findById("p2");
        verify(cache).put(any());
    }

    @Test
    void shouldSaveProductSuccessfully() {
        ProductRecord input = new ProductRecord("p3", "Plane", "Toy", "Kids", 800, "5-10", Map.of());
        ProductDocument saved = new ProductDocument("p3", "Plane", "Toy", "Kids", 800, "5-10", Map.of());

        when(repository.save(any())).thenReturn(saved);

        ProductRecord result = service.create(input);

        assertThat(result.id()).isEqualTo("p3");
        verify(repository).save(any());
        verify(cache).put(result);
    }
}
