package controller;

import models.*;
import java.util.List;

public class UserController {
    private DataStore dataStore;

    public UserController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void addUser(String name, String id, String role) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("ID cannot be empty");
        
        // Check for duplicate ID
        for (User u : dataStore.getUsers()) {
            if (u.getId().equals(id)) {
                throw new IllegalArgumentException("User ID already exists");
            }
        }

        User newUser;
        switch (role) {
            case "Student": newUser = new Student(name, id); break;
            case "Evaluator": newUser = new Evaluator(name, id); break;
            case "Coordinator": newUser = new Coordinator(name, id); break;
            default: throw new IllegalArgumentException("Invalid role");
        }
        dataStore.addUser(newUser);
    }

    public void removeUser(String id) {
        User toRemove = null;
        for (User u : dataStore.getUsers()) {
            if (u.getId().equals(id)) {
                toRemove = u;
                break;
            }
        }
        if (toRemove != null) {
            dataStore.removeUser(toRemove);
        }
    }

    public List<User> getAllUsers() {
        return dataStore.getUsers();
    }
}