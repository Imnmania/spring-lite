package me.niloybiswas.controllers;

import me.niloybiswas.dto.AddProductRequest;
import me.niloybiswas.dto.AddProductResponse;
import me.niloybiswas.dto.SearchResponse;
import me.niloybiswas.models.Product;
import me.niloybiswas.services.ProductService;
import me.niloybiswas.services.SearchService;

import java.util.List;

public class ProductController {

    private ProductService productService;
    private SearchService searchService;

    public ProductController(ProductService productService, SearchService searchService) {
        this.productService = productService;
        this.searchService = searchService;
    }

    public AddProductResponse addProduct(AddProductRequest request) {
        Product product = Product.builder().name(request.getName()).build();
        String id = productService.addProduct(product);
        return AddProductResponse.builder().id(id).build();
    }

    public Product getProduct(String id) {
        return productService.getProduct(id);
    }

    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    public SearchResponse searchProduct(String name) {
        List<Product> products = searchService.search(name);
        return SearchResponse.builder().products(products).build();
    }
}
