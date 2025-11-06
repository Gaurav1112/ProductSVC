package org.responsive.productsvc.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductDocument, String> {
    List<ProductDocument> findByType(String type);
    List<ProductDocument> findByCategory(String category);
}
