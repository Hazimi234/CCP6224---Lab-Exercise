import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final DataStore dataStore;

    public MainFrame() {
        setTitle("Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dataStore = new DataStore(); // Initialize the central data store

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

    // Method to store a new submission
    public void addSubmission(Submission submission) {
        dataStore.addSubmission(submission);
        System.out.println("Stored via DataStore: " + submission); 
    }

    public List<Submission> getSubmissions() { return dataStore.getSubmissions(); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}