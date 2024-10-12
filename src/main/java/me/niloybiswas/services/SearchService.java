package me.niloybiswas.services;

import me.niloybiswas.spring_lite.annotations.Autowired;
import me.niloybiswas.spring_lite.annotations.Component;
import me.niloybiswas.models.Product;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchService {

    @Autowired
    private ProductService productService;

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
