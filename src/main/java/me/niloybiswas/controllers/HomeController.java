package me.niloybiswas.controllers;

import jakarta.servlet.http.HttpServletResponse;
import me.niloybiswas.spring_lite.MethodType;
import me.niloybiswas.spring_lite.annotations.Component;
import me.niloybiswas.spring_lite.annotations.RequestMapping;
import me.niloybiswas.spring_lite.annotations.RestController;

import java.io.IOException;

@Component
@RestController
public class HomeController {

    @RequestMapping(url = "/", type = MethodType.GET)
    public void getHome(HttpServletResponse resp) throws IOException {
        resp.getWriter().write("<html><h1>Welcome to Spring Lite &#127811;</h1></html>");
    }
}
