package org.responsive.productsvc.service.impl;

import org.responsive.productsvc.cache.ProductCache;
import org.responsive.productsvc.dto.ProductRecord;
import org.responsive.productsvc.repository.ProductDocument;
import org.responsive.productsvc.repository.ProductRepository;
import org.responsive.productsvc.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductCache cache;

    public ProductServiceImpl(ProductRepository repository, ProductCache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    @Override
    public ProductRecord create(ProductRecord record) {
        ProductDocument doc = new ProductDocument(
            record.id(),
            record.name(),
            record.type(),
            record.category(),
            record.price(),
            record.recommendedAgeGroup(),
            record.attributes()
        );
        ProductDocument saved = repository.save(doc);
        ProductRecord result = toRecord(saved);
        cache.put(result);
        return result;
    }

    @Override
    public Optional<ProductRecord> getById(String id) {
        var cached = cache.getById(id);
        if (cached.isPresent()) return cached;

        return repository.findById(id)
                         .map(this::toRecord)
                         .map(record -> {
                             cache.put(record);
                             return record;
                         });
    }

    @Override
    public List<ProductRecord> getByType(String type) {
        var cached = cache.getByType(type);
        if (cached != null && !cached.isEmpty()) return cached;

        var records = repository.findByType(type).stream()
                                .map(this::toRecord)
                                .collect(Collectors.toList());
        records.forEach(cache::put);
        return records;
    }

    @Override
    public List<ProductRecord> getAll() {
        return repository.findAll().stream()
                         .map(this::toRecord)
                         .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
        cache.remove(id);
    }

    private ProductRecord toRecord(ProductDocument d) {
        return new ProductRecord(
            d.getId(),
            d.getName(),
            d.getType(),
            d.getCategory(),
            d.getPrice(),
            d.getRecommendedAgeGroup(),
            d.getAttributes()
        );
    }
}
