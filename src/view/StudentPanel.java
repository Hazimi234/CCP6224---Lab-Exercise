package view;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import controller.Submission;

public class StudentPanel extends JPanel {
    // Declare fields as class members so we can access them in the listeners
    private JTextField nameField;
    private JTextField titleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> typeCombo;
    private String selectedFilePath = "No file selected";

    public StudentPanel(MainFrame frame) {
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize the fields
        nameField = new JTextField();
        titleField = new JTextField();
        abstractArea = new JTextArea(3, 20);
        supervisorField = new JTextField();
        typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Research Title:")); form.add(titleField);
        form.add(new JLabel("Abstract:")); form.add(new JScrollPane(abstractArea));
        form.add(new JLabel("Supervisor:")); form.add(supervisorField);
        form.add(new JLabel("Presentation Type:")); form.add(typeCombo);
        
        JButton uploadBtn = new JButton("Select File...");
        form.add(new JLabel("Project File:")); form.add(uploadBtn);

        JButton submitBtn = new JButton("Submit Proposal");
        JButton backBtn = new JButton("Logout");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backBtn);
        buttonPanel.add(submitBtn);

        add(new JLabel("Student Submission Portal", SwingConstants.CENTER), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> frame.showScreen("Login"));

        // 1. Handle File Upload
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                selectedFilePath = file.getAbsolutePath();
                uploadBtn.setText(file.getName()); // Update button text to show filename
            }
        });

        // 2. Handle Submission
        submitBtn.addActionListener(e -> {
            Submission newSubmission = new Submission(
                nameField.getText(),
                titleField.getText(),
                abstractArea.getText(),
                supervisorField.getText(),
                (String) typeCombo.getSelectedItem(),
                selectedFilePath
            );
            
            frame.addSubmission(newSubmission);
            JOptionPane.showMessageDialog(this, "Proposal Submitted Successfully!");
            
            // Optional: Clear fields here if needed
            nameField.setText("");
            titleField.setText("");
            abstractArea.setText("");
            supervisorField.setText("");
            uploadBtn.setText("Select File...");
        });
    }
}