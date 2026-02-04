package view;

import java.awt.*;
import javax.swing.*;
import models.*;
import java.util.List;

/**
 * LoginPanel handles user authentication.
 * Instead of a password field,
 * it filters users by Role and allows selection from a dropdown.
 */
public class LoginPanel extends JPanel {
    private MainFrame parentFrame;
    private JComboBox<String> roleSelector;
    private JComboBox<String> userSelector;

    public LoginPanel(MainFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout()); // Centered Layout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // UI Components
        String[] roles = { "Student", "Evaluator", "Coordinator" };
        roleSelector = new JComboBox<>(roles);
        userSelector = new JComboBox<>();
        JButton loginBtn = new JButton("Login");
        JButton manageUsersBtn = new JButton("Manage Users (Admin)");

        // Layout Assembly
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Select Role:"), gbc);
        gbc.gridx = 1;
        add(roleSelector, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1;
        add(userSelector, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(loginBtn, gbc);
        gbc.gridy = 3;
        add(manageUsersBtn, gbc);

        // --- LISTENERS ---

        // When Role changes, update the list of Users
        roleSelector.addActionListener(e -> populateUsers());

        // Login Logic
        loginBtn.addActionListener(e -> {
            String selected = (String) roleSelector.getSelectedItem();
            String userStr = (String) userSelector.getSelectedItem();

            if (userStr == null) {
                JOptionPane.showMessageDialog(this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find the actual User object from the string "Name (ID)"
            for (User u : parentFrame.getUserController().getAllUsers()) {
                if ((u.getName() + " (" + u.getId() + ")").equals(userStr)) {
                    parentFrame.setCurrentUser(u); // Set Global Session
                    break;
                }
            }

            // Navigate to the appropriate dashboard
            parentFrame.showScreen(selected);
        });

        manageUsersBtn.addActionListener(e -> parentFrame.showScreen("UserManagement"));

        // Refresh list every time this screen appears
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                populateUsers();
            }
        });

        populateUsers(); // Initial load
    }

    /**
     * Filters the list of all users and shows only those matching the selected
     * Role.
     * Uses "instanceof" to check user type.
     */
    private void populateUsers() {
        userSelector.removeAllItems();
        String selectedRole = (String) roleSelector.getSelectedItem();
        if (selectedRole == null)
            return;

        List<User> users = parentFrame.getUserController().getAllUsers();
        for (User u : users) {
            boolean match = false;
            // Polymorphism check
            if (selectedRole.equals("Student") && u instanceof Student)
                match = true;
            else if (selectedRole.equals("Evaluator") && u instanceof Evaluator)
                match = true;
            else if (selectedRole.equals("Coordinator") && u instanceof Coordinator)
                match = true;

            if (match) {
                userSelector.addItem(u.getName() + " (" + u.getId() + ")");
            }
        }
    }
}