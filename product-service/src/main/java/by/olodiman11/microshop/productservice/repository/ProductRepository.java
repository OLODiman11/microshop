package by.olodiman11.microshop.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import by.olodiman11.microshop.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
