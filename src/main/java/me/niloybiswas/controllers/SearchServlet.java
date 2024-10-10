package me.niloybiswas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.niloybiswas.dto.SearchResponse;

import java.io.IOException;

public class SearchServlet extends HttpServlet {

    private ProductController productController;

    public SearchServlet(ProductController productController) {
        this.productController = productController;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchQuery = req.getParameter("query");
        SearchResponse searchResponse = productController.searchProduct(searchQuery);

        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(searchResponse));
    }
}
