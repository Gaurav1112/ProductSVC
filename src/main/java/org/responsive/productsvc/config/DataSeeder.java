package org.responsive.productsvc.config;

import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Map;

@Configuration
public class DataSeeder {

    private final MongoTemplate mongoTemplate;

    public DataSeeder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void seed() {
        if (!mongoTemplate.collectionExists("products")) {
            mongoTemplate.createCollection("products");
        }

        List<Document> sampleProducts = List.of(
            new Document(Map.of(
                "_id", "p1",
                "name", "Lego Starship",
                "type", "Toy",
                "category", "Kids",
                "price", 1999,
                "recommendedAgeGroup", "5-10",
                "attributes", Map.of("brand", "Lego", "pieces", "250")
            )),
            new Document(Map.of(
                "_id", "p2",
                "name", "Hot Wheels Jet Car",
                "type", "Toy",
                "category", "Kids",
                "price", 799,
                "recommendedAgeGroup", "5-10",
                "attributes", Map.of("brand", "Hot Wheels", "color", "red")
            )),
            new Document(Map.of(
                "_id", "p3",
                "name", "Barbie Dreamhouse",
                "type", "Toy",
                "category", "Kids",
                "price", 2999,
                "recommendedAgeGroup", "6-12",
                "attributes", Map.of("brand", "Barbie", "floors", "3")
            )),
            new Document(Map.of(
                "_id", "p4",
                "name", "Chess Board Classic",
                "type", "Game",
                "category", "Adults",
                "price", 1499,
                "recommendedAgeGroup", "10+",
                "attributes", Map.of("material", "wood", "pieces", "32")
            ))
        );

        sampleProducts.forEach(product -> {
            if (mongoTemplate.findById(product.get("_id"), Document.class, "products") == null) {
                mongoTemplate.insert(product, "products");
            }
        });

        System.out.println("✅ DataSeeder executed — sample products verified/inserted.");
    }
}
