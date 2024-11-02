package me.niloybiswas.repositories;

import me.niloybiswas.models.User;
import me.niloybiswas.spring_lite.annotations.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository {
    private Map<String, User> userMap;

    public UserRepository() {
        userMap = new HashMap<>();
    }

    public boolean register(User user) {
        if (userMap.containsKey(user.getUsername())) return false;
        userMap.put(user.getUsername(), user);
        return true;
    }

    public boolean passwordMatch(String username, String password) {
        if (!userMap.containsKey(username)) return false;
        return userMap.get(username).getPassword().equals(password);
    }

    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    public User getUser(String username) {
        return userMap.get(username);
    }

}
