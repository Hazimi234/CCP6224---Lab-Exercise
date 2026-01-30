import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}