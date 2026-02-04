package controller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Submission;
import models.Session;
import models.User;

public class DataStore {
    private List<Submission> submissions;
    private List<User> users;
    private List<Session> sessions;
    private static final String FILE_NAME = "submissions.dat";
    private static final String USER_FILE = "users.dat";
    private static final String SESSION_FILE = "sessions.dat";

    public DataStore() {
        submissions = new ArrayList<>();
        users = new ArrayList<>();
        sessions = new ArrayList<>();
        load();
        loadUsers();
        loadSessions();
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        save();
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void removeUser(User user) {
        users.remove(user);
        saveUsers();
    }

    public void addSession(Session session) {
        sessions.add(session);
        saveSessions();
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        saveSessions();
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void saveSubmissions() {
        save();
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(submissions);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

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