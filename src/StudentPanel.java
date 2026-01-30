import java.awt.*;
import javax.swing.*;

public class StudentPanel extends JPanel {
    public StudentPanel(MainFrame frame) {
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Project Title:")); form.add(new JTextField());
        form.add(new JLabel("Abstract:")); form.add(new JScrollPane(new JTextArea(3, 20)));
        form.add(new JLabel("Supervisor:")); form.add(new JTextField());
        form.add(new JLabel("Type:")); form.add(new JComboBox<>(new String[]{"Research", "Development"}));
        
        JButton uploadBtn = new JButton("Select File...");
        form.add(new JLabel("Project File:")); form.add(uploadBtn);

        JButton submitBtn = new JButton("Submit Proposal");
        JButton backBtn = new JButton("Logout");
        
        form.add(backBtn); form.add(submitBtn);

        add(new JLabel("Student Submission Portal", SwingConstants.CENTER), BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        backBtn.addActionListener(e -> frame.showScreen("Login"));
    }
}