package view;
import java.awt.*;
import javax.swing.*;

public class LoginPanel extends JPanel {
    // We store a reference to the main window so we can "talk" to it
    private MainFrame parentFrame;

    public LoginPanel(MainFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] roles = {"Student", "Evaluator", "Coordinator"};
        JComboBox<String> roleSelector = new JComboBox<>(roles);
        JButton loginBtn = new JButton("Login");

        // Add components to THIS panel
        add(new JLabel("Select Role:"), gbc);
        add(roleSelector, gbc);
        gbc.gridy = 1;
        add(loginBtn, gbc);

        // This is where the magic happens
        loginBtn.addActionListener(e -> {
            String selected = (String) roleSelector.getSelectedItem();
            // Call the method in MainFrame to switch screens
            parentFrame.showScreen(selected); 
        });
    }
}