package view;

import controller.*;
import java.awt.*;
import models.Submission;
import models.User;
import java.util.List;
import javax.swing.*;

/**
 * MainFrame is the primary window (JFrame) of the application.
 * It uses the "CardLayout" strategy to switch between different screens
 * (Login, Student, Coordinator) within the same window.
 * * It also initializes all Controllers and the central DataStore.
 */
public class MainFrame extends JFrame {
    // CardLayout allows us to stack panels like a deck of cards and show only one
    // at a time.
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // Core Controllers (The "Brains" of the application)
    private final DataStore dataStore;
    private final SubmissionController submissionController;
    private final UserController userController;
    private final CoordinatorController coordinatorController;

    // Tracks who is currently logged in
    private User currentUser;

    public MainFrame() {
        setTitle("Seminar Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 1. Initialize the Central Data and Controllers
        dataStore = new DataStore();
        submissionController = new SubmissionController(dataStore);
        userController = new UserController(dataStore);
        coordinatorController = new CoordinatorController(dataStore);

        // 2. Instantiate and Register all Screens (Panels)
        mainPanel.add(new LoginPanel(this), "Login");
        mainPanel.add(new StudentPanel(this), "Student");
        mainPanel.add(new EvaluatorPanel(this), "Evaluator");
        mainPanel.add(new CoordinatorPanel(this), "Coordinator");
        mainPanel.add(new UserManagementPanel(this), "UserManagement");

        add(mainPanel);
        setVisible(true);
    }

    // Switches the current view to the screen specified by the name.

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    // --- GETTERS & SETTERS (Shared Context) ---

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Accessors for child panels to reach the controllers
    public SubmissionController getSubmissionController() {
        return submissionController;
    }

    public UserController getUserController() {
        return userController;
    }

    public CoordinatorController getCoordinatorController() {
        return coordinatorController;
    }

    // Helper to get raw list of submissions
    public List<Submission> getSubmissions() {
        return dataStore.getSubmissions();
    }
}