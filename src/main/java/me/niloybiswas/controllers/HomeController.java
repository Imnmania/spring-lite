package me.niloybiswas.controllers;

import jakarta.servlet.http.HttpServletResponse;
import me.niloybiswas.dto.GenericResponse;
import me.niloybiswas.spring_lite.annotations.GetMapping;
import me.niloybiswas.spring_lite.annotations.RestController;
import me.niloybiswas.spring_lite.annotations.Value;

import java.io.IOException;

@RestController
public class HomeController {

    @Value(key = "${dev.value}")
    private String profileName;

    @GetMapping(url = "/")
    public void getHome(HttpServletResponse resp) throws IOException {
        resp.getWriter().write("<html><h1>Welcome to Spring Lite &#127811;</h1></html>");
    }

    @GetMapping(url = "/profileName")
    public GenericResponse getProfileName() {
        return new GenericResponse(200, profileName);
    }
}
