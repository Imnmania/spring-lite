package me.niloybiswas.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.niloybiswas.spring_lite.annotations.Servlet;

import java.io.IOException;

@Servlet(urlMapping = "/")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<html><h1>Welcome to Spring Lite &#127811;</h1></html>");
    }
}
