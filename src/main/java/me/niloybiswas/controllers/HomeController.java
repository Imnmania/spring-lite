package me.niloybiswas.controllers;

import jakarta.servlet.http.HttpServletResponse;
import me.niloybiswas.spring_lite.annotations.GetMapping;
import me.niloybiswas.spring_lite.annotations.RequestMapping;
import me.niloybiswas.spring_lite.annotations.RestController;

import java.io.IOException;

@RestController
@RequestMapping(url = "/")
public class HomeController {

    @GetMapping
    public void getHome(HttpServletResponse resp) throws IOException {
        resp.getWriter().write("<html><h1>Welcome to Spring Lite &#127811;</h1></html>");
    }
}
