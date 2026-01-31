package view;
import java.awt.*;
import javax.swing.*;
import java.io.File;

public class StudentPanel extends JPanel {
    // Declare fields as class members so we can access them in the listeners
    private JTextField titleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> typeCombo;
    private String selectedFilePath = null;

    public StudentPanel(MainFrame frame) {
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize the fields
        titleField = new JTextField();
        abstractArea = new JTextArea(3, 20);
        supervisorField = new JTextField();
        typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});

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

        add(new JLabel("Seminar Registration Form", SwingConstants.CENTER), BorderLayout.NORTH);
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
            try {
                String studentName = "Unknown";
                if (frame.getCurrentUser() != null) {
                    studentName = frame.getCurrentUser().getName();
                }

                frame.getSubmissionController().submitProposal(
                    studentName,
                    titleField.getText(),
                    abstractArea.getText(),
                    supervisorField.getText(),
                    (String) typeCombo.getSelectedItem(),
                    selectedFilePath
                );
                JOptionPane.showMessageDialog(this, "Proposal Submitted Successfully!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Submission Failed", JOptionPane.ERROR_MESSAGE);
                return; // Don't clear fields if error
            }
            
            // Optional: Clear fields here if needed
            titleField.setText("");
            abstractArea.setText("");
            supervisorField.setText("");
            uploadBtn.setText("Select File...");
        });
    }
}