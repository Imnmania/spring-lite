package me.niloybiswas.services;

import me.niloybiswas.models.Product;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private ProductService productService;

    public SearchService(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> search(String name) {
        List<Product> filteredProducts = new ArrayList<>();
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

}
