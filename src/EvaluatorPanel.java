import java.awt.*;
import javax.swing.*;

public class EvaluatorPanel extends JPanel {
    public EvaluatorPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        // List of submissions (Left side)
        String[] columns = {"ID", "Student Name", "Status"};
        Object[][] data = { {"1", "John Doe", "Pending"}, {"2", "Jane Smith", "Pending"} };
        JTable table = new JTable(data, columns);
        
        // Rubric Form (Right side)
        JPanel rubric = new JPanel(new GridLayout(5, 1, 10, 10));
        rubric.setBorder(BorderFactory.createTitledBorder("Evaluation Rubric"));
        rubric.add(new JLabel("Content Score (1-10):")); rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 10, 1)));
        rubric.add(new JLabel("Comments:")); rubric.add(new JTextField());
        
        JButton saveBtn = new JButton("Save Evaluation");
        JButton backBtn = new JButton("Logout");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn); btnPanel.add(backBtn);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(table), rubric);
        splitPane.setDividerLocation(350);

        add(splitPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> frame.showScreen("Login"));
    }
}