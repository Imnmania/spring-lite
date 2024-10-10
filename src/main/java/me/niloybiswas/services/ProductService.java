package me.niloybiswas.services;

import me.niloybiswas.models.Product;
import me.niloybiswas.repositories.ProductRepository;

import java.util.List;
import java.util.UUID;

public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String addProduct(Product product) {
        String id = UUID.randomUUID().toString();
        product.setId(id);
        boolean success = productRepository.addProduct(product);
        if (success) {
            return id;
        }
        return "n/a";
    }

    public Product getProduct(String id) {
        if (id == null) {
            return null;
        }
        return productRepository.getProduct(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.getProducts();
    }

}
