package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Submission;
import models.Evaluation;
import models.Session;
import models.User;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * EvaluatorPanel allows lecturers to view assigned submissions and grade them.
 * Key Features:
 * 1. Filtered Table: Shows only submissions assigned to the logged-in
 * evaluator.
 * 2. Rubric Form: Inputs for the 4 grading criteria.
 * 3. File Opening: Can click to open the student's PDF/PPT.
 */
public class EvaluatorPanel extends JPanel {
    private MainFrame frame;
    private DefaultTableModel tableModel;
    private List<Submission> displayedSubmissions = new ArrayList<>();

    public EvaluatorPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        // --- LEFT SIDE: Table of Submissions ---
        String[] columns = { "Student Name", "Project Title", "Abstract", "Presentation Type", "File Path", "Status",
                "Action" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only the "View File" button is clickable
            }
        };
        JTable table = new JTable(tableModel);

        // --- RIGHT SIDE: Rubric Form ---
        JPanel rubric = new JPanel(new GridLayout(0, 2, 10, 10));
        rubric.setBorder(BorderFactory.createTitledBorder("Evaluation Rubric"));

        // Spinners for marks (0-5)
        rubric.add(new JLabel("Problem Clarity (1-5):"));
        rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        rubric.add(new JLabel("Methodology (1-5):"));
        rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        rubric.add(new JLabel("Results (1-5):"));
        rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        rubric.add(new JLabel("Presentation (1-5):"));
        rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        rubric.add(new JLabel("Comments:"));
        rubric.add(new JTextField());

        // Setup custom button inside the table
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), table));

        // Buttons
        JButton saveBtn = new JButton("Save Evaluation");
        JButton backBtn = new JButton("Logout");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(backBtn);

        // Split Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(table), rubric);
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // --- LISTENERS ---
        backBtn.addActionListener(e -> frame.showScreen("Login"));

        saveBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Gather data from form
                int clarity = (int) ((JSpinner) rubric.getComponent(1)).getValue();
                int methodology = (int) ((JSpinner) rubric.getComponent(3)).getValue();
                int results = (int) ((JSpinner) rubric.getComponent(5)).getValue();
                int presentation = (int) ((JSpinner) rubric.getComponent(7)).getValue();
                String comments = ((JTextField) rubric.getComponent(9)).getText();

                // Create Evaluation Object
                User currentUser = frame.getCurrentUser();
                Submission submission = displayedSubmissions.get(selectedRow);

                Evaluation evaluation = new Evaluation(
                        currentUser.getId(),
                        currentUser.getName(),
                        clarity, methodology, results, presentation, comments);

                // Save Logic
                submission.addEvaluation(evaluation);
                frame.getSubmissionController().saveSubmissions();

                JOptionPane.showMessageDialog(this, "Evaluation saved for " + submission.getName());
                ((JTextField) rubric.getComponent(9)).setText(""); // Clear text
                refreshTable(); // Update status
            } else {
                JOptionPane.showMessageDialog(this, "Please select a submission to evaluate.");
            }
        });

        // Auto-refresh when opening panel
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshTable();
            }
        });
    }

    /**
     * Filters logic: Only shows submissions that are part of a Session
     * where the current User is an assigned Evaluator.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        displayedSubmissions.clear();
        User currentUser = frame.getCurrentUser();
        if (currentUser == null)
            return;

        // 1. Find all Submissions assigned to me via Sessions
        Set<String> assignedSubmissionIds = new HashSet<>();
        List<Session> sessions = frame.getCoordinatorController().getAllSessions();

        for (Session session : sessions) {
            // If evaluator is in this session's evaluator list,
            List<String> evalIds = session.getAssignedEvaluatorIds();
            if (evalIds != null && evalIds.contains(currentUser.getId())) {
                // then the evaluator must grade these submissions
                List<String> subIds = session.getAssignedSubmissionIds();
                if (subIds != null) {
                    assignedSubmissionIds.addAll(subIds);
                }
            }
        }

        // 2. Populate Table
        for (Submission s : frame.getSubmissions()) {
            if (assignedSubmissionIds.contains(s.getId())) {
                // Check if the evaluator have already graded it
                String myStatus = "Pending";
                for (Evaluation e : s.getEvaluations()) {
                    if (e.getEvaluatorId().equals(currentUser.getId())) {
                        myStatus = "Evaluated";
                        break;
                    }
                }
                displayedSubmissions.add(s);
                tableModel.addRow(new Object[] {
                        s.getName(), s.getTitle(), s.getAbstractText(),
                        s.getPresentationType(), s.getFilePath(), myStatus, "View File"
                });
            }
        }
    }
}

// Custom Classes for Table Buttons (View File)
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            String filePath = (String) table.getValueAt(row, 4);
            openFile(filePath);
            fireEditingStopped();
        });
    }

    // Uses Java Desktop API to open PDF/PPT
    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists())
                Desktop.getDesktop().open(file);
            else
                JOptionPane.showMessageDialog(null, "File not found: " + filePath);
        } catch (IOException | SecurityException ex) {
            JOptionPane.showMessageDialog(null, "Error opening file.");
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "View" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}