package org.responsive.productsvc.cache;

import org.responsive.productsvc.dto.ProductRecord;
import java.util.List;
import java.util.Optional;

public interface ProductCache {
    Optional<ProductRecord> getById(String id);
    List<ProductRecord> getByType(String type);
    void put(ProductRecord product);
    void remove(String id);
    void clear();
}
