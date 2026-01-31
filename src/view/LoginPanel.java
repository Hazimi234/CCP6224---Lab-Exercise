package view;
import java.awt.*;
import javax.swing.*;
import models.*;
import java.util.List;

public class LoginPanel extends JPanel {
    // We store a reference to the main window so we can "talk" to it
    private MainFrame parentFrame;
    private JComboBox<String> roleSelector;
    private JComboBox<String> userSelector;

    public LoginPanel(MainFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] roles = {"Student", "Evaluator", "Coordinator"};
        roleSelector = new JComboBox<>(roles);
        userSelector = new JComboBox<>();
        JButton loginBtn = new JButton("Login");
        JButton manageUsersBtn = new JButton("Manage Users");

        // Add components to THIS panel
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Select Role:"), gbc);
        gbc.gridx = 1;
        add(roleSelector, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1;
        add(userSelector, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        add(loginBtn, gbc);
        gbc.gridy = 3;
        add(manageUsersBtn, gbc);

        // Listeners
        roleSelector.addActionListener(e -> populateUsers());

        // This is where the magic happens
        loginBtn.addActionListener(e -> {
            String selected = (String) roleSelector.getSelectedItem();
            String user = (String) userSelector.getSelectedItem();
            
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Please select a user.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Find the User object based on the selection string
            for (User u : parentFrame.getUserController().getAllUsers()) {
                if ((u.getName() + " (" + u.getId() + ")").equals(user)) {
                    parentFrame.setCurrentUser(u);
                    break;
                }
            }

            // Call the method in MainFrame to switch screens
            parentFrame.showScreen(selected); 
        });

        manageUsersBtn.addActionListener(e -> parentFrame.showScreen("UserManagement"));

        // Refresh user list when panel is shown
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                populateUsers();
            }
        });
        
        populateUsers(); // Initial population
    }

    private void populateUsers() {
        userSelector.removeAllItems();
        String selectedRole = (String) roleSelector.getSelectedItem();
        if (selectedRole == null) return;

        List<User> users = parentFrame.getUserController().getAllUsers();
        for (User u : users) {
            boolean match = false;
            if (selectedRole.equals("Student") && u instanceof Student) match = true;
            else if (selectedRole.equals("Evaluator") && u instanceof Evaluator) match = true;
            else if (selectedRole.equals("Coordinator") && u instanceof Coordinator) match = true;

            if (match) {
                userSelector.addItem(u.getName() + " (" + u.getId() + ")");
            }
        }
    }
}