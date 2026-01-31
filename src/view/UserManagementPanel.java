package view;

import controller.UserController;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private MainFrame frame;
    private DefaultTableModel tableModel;

    public UserManagementPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));

        // Top: Form
        JPanel formPanel = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(10);
        JTextField idField = new JTextField(10);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Student", "Evaluator", "Coordinator"});
        JButton addBtn = new JButton("Add User");

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("ID:")); formPanel.add(idField);
        formPanel.add(new JLabel("Role:")); formPanel.add(roleCombo);
        formPanel.add(addBtn);

        // Center: Table
        String[] columns = {"Name", "ID", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        
        // Bottom: Actions
        JPanel bottomPanel = new JPanel();
        JButton removeBtn = new JButton("Remove Selected");
        JButton backBtn = new JButton("Back to Login");
        bottomPanel.add(removeBtn);
        bottomPanel.add(backBtn);

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        addBtn.addActionListener(e -> {
            try {
                frame.getUserController().addUser(
                    nameField.getText(),
                    idField.getText(),
                    (String) roleCombo.getSelectedItem()
                );
                refreshTable();
                nameField.setText("");
                idField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = (String) tableModel.getValueAt(row, 1);
                frame.getUserController().removeUser(id);
                refreshTable();
            }
        });

        backBtn.addActionListener(e -> frame.showScreen("Login"));

        // Refresh when shown
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<User> users = frame.getUserController().getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getName(),
                u.getId(),
                u.getClass().getSimpleName()
            });
        }
    }
}