package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Submission;
import models.Session;
import models.User;

/**
 * DataStore acts as a centralized database replacement.
 * It holds lists of Users, Submissions, and Sessions in memory
 * and handles saving/loading them to binary files (.dat) using Java
 * Serialization.
 */
public class DataStore {
    // In-memory lists to hold our application data
    private List<Submission> submissions;
    private List<User> users;
    private List<Session> sessions;

    // File names for persistent storage
    private static final String FILE_NAME = "submissions.dat";
    private static final String USER_FILE = "users.dat";
    private static final String SESSION_FILE = "sessions.dat";

    public DataStore() {
        // Initialize lists to avoid NullPointerExceptions
        submissions = new ArrayList<>();
        users = new ArrayList<>();
        sessions = new ArrayList<>();

        // Load existing data from files when the application starts
        load();
        loadUsers();
        loadSessions();
    }

    // --- SUBMISSION MANAGEMENT ---

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        save(); // Save immediately to disk
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void saveSubmissions() {
        save(); // Public method to force a save (e.g., after editing a vote)
    }

    // Saves the list of submissions to "submissions.dat"
    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(submissions);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // Loads the list of submissions from "submissions.dat"
    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                submissions = (List<Submission>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }
    }

    // --- USER MANAGEMENT ---

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void removeUser(User user) {
        users.remove(user);
        saveUsers();
    }

    public List<User> getUsers() {
        return users;
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(USER_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading users: " + e.getMessage());
            }
        }
    }

    // --- SESSION MANAGEMENT ---

    public void addSession(Session session) {
        sessions.add(session);
        saveSessions();
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        saveSessions();
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void saveSessions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))) {
            oos.writeObject(sessions);
        } catch (IOException e) {
            System.err.println("Error saving sessions: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSessions() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                sessions = (List<Session>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading sessions: " + e.getMessage());
            }
        }
    }
}