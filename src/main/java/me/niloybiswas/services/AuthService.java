package me.niloybiswas.services;

import me.niloybiswas.models.User;
import me.niloybiswas.repositories.UserRepository;
import me.niloybiswas.spring_lite.annotations.Autowired;
import me.niloybiswas.spring_lite.annotations.Component;

import java.util.List;

@Component
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        user = user.toBuilder()
                .username(user.getName().toLowerCase())
                .build();
        boolean success = userRepository.register(user);
        if (success) return user;
        return null;
    }

    public User signIn(String username, String password) {
        if (userRepository.passwordMatch(username, password)) return userRepository.getUser(username);
        return null;
    }

    public User getUser(String username) {
        return userRepository.getUser(username);
    }

    public List<User> getAllUsers() {
        return userRepository.getUsers();
    }

}
