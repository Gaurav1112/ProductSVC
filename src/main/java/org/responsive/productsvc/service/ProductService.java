package org.responsive.productsvc.service;

import org.responsive.productsvc.dto.ProductRecord;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductRecord create(ProductRecord record);
    Optional<ProductRecord> getById(String id);
    List<ProductRecord> getByType(String type);
    List<ProductRecord> getAll();
    void delete(String id);
}
