package by.olodiman11.microshop.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import by.olodiman11.microshop.productservice.dto.ProductRequest;
import by.olodiman11.microshop.productservice.dto.ProductResponse;
import by.olodiman11.microshop.productservice.model.Product;
import by.olodiman11.microshop.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());

        productRepository.save(product);
        log.info("Product {}  is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
            .map(this::mapToProductResponse)
            .toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
    }
}
