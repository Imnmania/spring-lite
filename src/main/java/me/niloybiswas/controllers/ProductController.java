package me.niloybiswas.controllers;

import me.niloybiswas.spring_lite.MethodType;
import me.niloybiswas.spring_lite.annotations.*;
import me.niloybiswas.dto.AddProductRequest;
import me.niloybiswas.dto.AddProductResponse;
import me.niloybiswas.dto.SearchResponse;
import me.niloybiswas.models.Product;
import me.niloybiswas.services.ProductService;
import me.niloybiswas.services.SearchService;

import java.util.List;

@Component
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SearchService searchService;

    @RequestMapping(url = "/api/products", type = MethodType.POST)
    public AddProductResponse addProduct(@RequestBody AddProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());

        String id = productService.addProduct(product);

        AddProductResponse addProductResponse = new AddProductResponse();
        addProductResponse.setId(id);

        return addProductResponse;
    }

    @RequestMapping(url = "/api/products/{id}", type = MethodType.GET)
    public Product getProduct(@PathVariable(value = "id") String id) {
        return productService.getProduct(id);
    }

    @RequestMapping(url = "/api/products", type = MethodType.GET)
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }
}
