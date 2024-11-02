package me.niloybiswas.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import me.niloybiswas.dto.LoginRequest;
import me.niloybiswas.dto.RegisterRequest;
import me.niloybiswas.dto.RegisterResponse;
import me.niloybiswas.models.User;
import me.niloybiswas.services.AuthService;
import me.niloybiswas.spring_lite.annotations.*;

@RestController
@RequestMapping(url = "/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(url = "/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        System.out.println("request = " + request);
        User user = User.builder()
                .name(request.getName())
                .password(request.getPassword())
                .build();
        System.out.println("user = " + user);
        user = authService.register(user);
        return RegisterResponse.builder()
                .user(user)
                .build();
    };

    @PostMapping(url = "/login")
    public User login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        User user = authService.signIn(loginRequest.getUsername(), loginRequest.getPassword());
        if (user.getUsername().equals(loginRequest.getUsername())) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("username", user.getUsername());
        }
        return user;
    }

    @Authenticated
    @GetMapping(url = "/users/{id}")
    public User getUserById(@PathVariable(value = "id") String id) {
        return authService.getUser(id);
    }
}
