package me.niloybiswas.controllers;

import me.niloybiswas.spring_lite.annotations.Component;
import me.niloybiswas.dto.AddProductRequest;
import me.niloybiswas.dto.AddProductResponse;
import me.niloybiswas.dto.SearchResponse;
import me.niloybiswas.models.Product;
import me.niloybiswas.services.ProductService;
import me.niloybiswas.services.SearchService;

import java.util.List;

@Component
public class ProductController {

    private ProductService productService;
    private SearchService searchService;

    public AddProductResponse addProduct(AddProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());

        String id = productService.addProduct(product);

        AddProductResponse addProductResponse = new AddProductResponse();
        addProductResponse.setId(id);

        return addProductResponse;
    }

    public Product getProduct(String id) {
        return productService.getProduct(id);
    }

    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    public SearchResponse searchProduct(String name) {
        List<Product> products = searchService.search(name);
        return new SearchResponse(products);
    }
}
