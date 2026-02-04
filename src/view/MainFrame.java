package view;
import controller.*;
import java.awt.*;
import models.Submission;
import models.User;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final DataStore dataStore;
    private final SubmissionController submissionController;
    private final UserController userController;
    private final CoordinatorController coordinatorController;
    private User currentUser;

    public MainFrame() {
        setTitle("Seminar Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dataStore = new DataStore(); // Initialize the central data store
        submissionController = new SubmissionController(dataStore); // Initialize controller
        userController = new UserController(dataStore);
        coordinatorController = new CoordinatorController(dataStore);

        // You "instantiate" the other classes here
        mainPanel.add(new LoginPanel(this), "Login");
        mainPanel.add(new StudentPanel(this), "Student");
        mainPanel.add(new EvaluatorPanel(this), "Evaluator");
        mainPanel.add(new CoordinatorPanel(this), "Coordinator");
        mainPanel.add(new UserManagementPanel(this), "UserManagement");

        add(mainPanel);
        setVisible(true);
    }

    // Method to allow other panels to switch screens
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public SubmissionController getSubmissionController() {
        return submissionController;
    }

    public UserController getUserController() {
        return userController;
    }

    public CoordinatorController getCoordinatorController() {
        return coordinatorController;
    }

    public List<Submission> getSubmissions() { return dataStore.getSubmissions(); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}