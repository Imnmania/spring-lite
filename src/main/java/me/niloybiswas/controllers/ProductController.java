package me.niloybiswas.controllers;

import me.niloybiswas.spring_lite.annotations.*;
import me.niloybiswas.dto.AddProductRequest;
import me.niloybiswas.dto.AddProductResponse;
import me.niloybiswas.models.Product;
import me.niloybiswas.services.ProductService;
import me.niloybiswas.services.SearchService;

import java.util.List;

@RestController
@RequestMapping(url = "/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SearchService searchService;

    @PostMapping
    public AddProductResponse addProduct(@RequestBody AddProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());

        String id = productService.addProduct(product);

        AddProductResponse addProductResponse = new AddProductResponse();
        addProductResponse.setId(id);

        return addProductResponse;
    }

    @GetMapping(url = "/{id}")
    public Product getProduct(@PathVariable(value = "id") String id) {
        return productService.getProduct(id);
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }
}
