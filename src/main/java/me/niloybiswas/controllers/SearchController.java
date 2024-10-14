package me.niloybiswas.controllers;

import me.niloybiswas.dto.SearchResponse;
import me.niloybiswas.models.Product;
import me.niloybiswas.services.SearchService;
import me.niloybiswas.spring_lite.annotations.*;

import java.util.List;

@RestController
@RequestMapping(url = "/api/products")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(url = "/search")
    public SearchResponse searchProduct(@RequestParam(value = "query") String query) {
        List<Product> products = searchService.search(query);
        return new SearchResponse(products);
    }
}
