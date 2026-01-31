package view;
import controller.*;
import java.awt.*;
import models.Submission;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final DataStore dataStore;
    private final SubmissionController submissionController;

    public MainFrame() {
        setTitle("Seminar Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dataStore = new DataStore(); // Initialize the central data store
        submissionController = new SubmissionController(dataStore); // Initialize controller

        // You "instantiate" the other classes here
        mainPanel.add(new LoginPanel(this), "Login");
        mainPanel.add(new StudentPanel(this), "Student");
        mainPanel.add(new EvaluatorPanel(this), "Evaluator");
        mainPanel.add(new CoordinatorPanel(this), "Coordinator");

        add(mainPanel);
        setVisible(true);
    }

    // Method to allow other panels to switch screens
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public SubmissionController getSubmissionController() {
        return submissionController;
    }

    public List<Submission> getSubmissions() { return dataStore.getSubmissions(); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}