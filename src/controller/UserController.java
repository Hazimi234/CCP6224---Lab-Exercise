package controller;

import models.*;
import java.util.List;

/**
 * UserController handles the business logic for managing Users.
 * It is responsible for creating specific user types (Student, Evaluator,
 * Coordinator)
 * and validating user inputs.
 */
public class UserController {
    private DataStore dataStore;

    // Constructor receives DataStore to access the central data
    public UserController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Creates a new user and adds them to the system.
     * Uses a switch statement to determine which subclass
     * (Student/Evaluator/Coordinator) to instantiate.
     */
    public void addUser(String name, String id, String role) {
        // 1. Validation
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID cannot be empty");

        // 2. Check for Duplicate IDs (Business Rule)
        for (User u : dataStore.getUsers()) {
            if (u.getId().equals(id)) {
                throw new IllegalArgumentException("User ID already exists");
            }
        }

        // 3. Factory Logic: Create the correct object based on the role string
        User newUser;
        switch (role) {
            case "Student":
                newUser = new Student(name, id);
                break;
            case "Evaluator":
                newUser = new Evaluator(name, id);
                break;
            case "Coordinator":
                newUser = new Coordinator(name, id);
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }

        // 4. Persist the new user
        dataStore.addUser(newUser);
    }

    // Removes a user from the system by their ID.

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

    // Retrieves all users for listing in the UI
    public List<User> getAllUsers() {
        return dataStore.getUsers();
    }
}