package me.niloybiswas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.niloybiswas.spring_lite.annotations.Autowired;
import me.niloybiswas.spring_lite.annotations.Servlet;
import me.niloybiswas.dto.AddProductRequest;
import me.niloybiswas.dto.AddProductResponse;
import me.niloybiswas.dto.SearchResponse;
import me.niloybiswas.models.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Servlet(urlMapping = "/api/products/*")
public class ProductServlet extends HttpServlet {

    @Autowired
    private ProductController productController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        System.out.println("uri = " + uri);

        String id = extractIdFromUri(uri);
        System.out.println("id = " + id);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();

        if (id == null) {
            List<Product> products = productController.getProducts();
            SearchResponse searchResponse = new SearchResponse(products);
            resp.getWriter().write(objectMapper.writeValueAsString(searchResponse));
            return;
        }

        Product product = productController.getProduct(id);
        resp.getWriter().write(objectMapper.writeValueAsString(product));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = readJson(req);
        System.out.println("json = " + json);

        ObjectMapper objectMapper = new ObjectMapper();
        AddProductRequest request = objectMapper.readValue(json, AddProductRequest.class);
        System.out.println("request = " + request);
        AddProductResponse response = productController.addProduct(request);
        System.out.println("response = " + response);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }

    private String extractIdFromUri(String uri) {
        String[] segments = uri.split("/");
        Pattern UUID_REGEX =
                Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        if (!UUID_REGEX.matcher(segments[segments.length - 1]).matches())return null;
        return segments[segments.length - 1];
    }

    private String readJson(HttpServletRequest req) {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonBuilder.toString();
    }
}
