package view;

import java.awt.*;
import javax.swing.*;
import java.io.File;

/**
 * StudentPanel provides the UI for students to register their seminars.
 * It includes a form for Title, Abstract, Supervisor, and File Upload.
 */
public class StudentPanel extends JPanel {
    private JTextField titleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> typeCombo;
    private String selectedFilePath = null;

    public StudentPanel(MainFrame frame) {
        setLayout(new BorderLayout(10, 10));

        // Form Layout
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleField = new JTextField();
        abstractArea = new JTextArea(3, 20);
        supervisorField = new JTextField();
        typeCombo = new JComboBox<>(new String[] { "Oral", "Poster" });

        form.add(new JLabel("Research Title:"));
        form.add(titleField);
        form.add(new JLabel("Abstract:"));
        form.add(new JScrollPane(abstractArea));
        form.add(new JLabel("Supervisor:"));
        form.add(supervisorField);
        form.add(new JLabel("Presentation Type:"));
        form.add(typeCombo);

        JButton uploadBtn = new JButton("Select File...");
        form.add(new JLabel("Project File:"));
        form.add(uploadBtn);

        // Buttons
        JButton submitBtn = new JButton("Submit Proposal");
        JButton backBtn = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backBtn);
        buttonPanel.add(submitBtn);

        add(new JLabel("Seminar Registration Form", SwingConstants.CENTER), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- LISTENERS ---

        backBtn.addActionListener(e -> frame.showScreen("Login"));

        // File Chooser Logic
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                selectedFilePath = file.getAbsolutePath(); // Store path only
                uploadBtn.setText(file.getName());
            }
        });

        // Submission Logic
        submitBtn.addActionListener(e -> {
            try {
                String studentName = "Unknown";
                if (frame.getCurrentUser() != null) {
                    studentName = frame.getCurrentUser().getName();
                }

                // Pass data to Controller
                frame.getSubmissionController().submitProposal(
                        studentName,
                        titleField.getText(),
                        abstractArea.getText(),
                        supervisorField.getText(),
                        (String) typeCombo.getSelectedItem(),
                        selectedFilePath);

                JOptionPane.showMessageDialog(this, "Proposal Submitted Successfully!");

                // Clear form
                titleField.setText("");
                abstractArea.setText("");
                supervisorField.setText("");
                uploadBtn.setText("Select File...");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}