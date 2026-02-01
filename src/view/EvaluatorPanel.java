package view;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Submission;
import models.Session;
import models.User;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor{
    protected JButton button;
    private String label;
    private final JTable table;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            int row = table.getSelectedRow();
            String filePath = (String) table.getValueAt(row, 4); // Assuming file path is in column 4
            openFile(filePath);
            fireEditingStopped();
            });
    }
    private void openFile(String filePath) {
        try {
            File file= new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(null, "File not found: " + filePath);
            }
        } catch (IOException | SecurityException ex) {
            JOptionPane.showMessageDialog(null, "Error opening file: " + ex.getMessage());}
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "View" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}

public class EvaluatorPanel extends JPanel {
    public EvaluatorPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        // List of submissions (Left side)
        String[] columns = {"Student Name", "Project Title", "Abstract", "Presentation Type", "File Path", "Status", "Action"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {return column == 6; // Only the "Action" column is editable
                }
        };
        JTable table = new JTable(tableModel);
        
        // Rubric Form (Right side)
        JPanel rubric = new JPanel(new GridLayout(0, 2, 10, 10));
        rubric.setBorder(BorderFactory.createTitledBorder("Evaluation Rubric"));
        rubric.add(new JLabel("Problem Clarity (1-5):")); rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        rubric.add(new JLabel("Methodology (1-5):")); rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        rubric.add(new JLabel("Results (1-5):")); rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        rubric.add(new JLabel("Presentation (1-5):")); rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 5, 1)));
        // rubric.add(new JLabel("Content Score (1-10):")); rubric.add(new JSpinner(new SpinnerNumberModel(5, 0, 10, 1)));
        rubric.add(new JLabel("Comments:")); rubric.add(new JTextField());

        //table button setup
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), table));
        
        JButton saveBtn = new JButton("Save Evaluation");
        JButton backBtn = new JButton("Logout");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn); btnPanel.add(backBtn);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(table), rubric);
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> frame.showScreen("Login"));

        //save button functionality
        saveBtn.addActionListener(e ->{
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1){
                int clarity = (int) ((JSpinner) rubric.getComponent(1)).getValue();
                int methodology = (int) ((JSpinner) rubric.getComponent(3)).getValue();
                int results = (int) ((JSpinner) rubric.getComponent(5)).getValue();
                int presentation = (int) ((JSpinner) rubric.getComponent(7)).getValue();
                String comments = ((JTextField) rubric.getComponent(9)).getText();

                Submission submission = frame.getSubmissions().get(selectedRow);
                submission.setScores(clarity, methodology, results, presentation);
                submission.setComments(comments);
                submission.setStatus("Evaluated");

                JOptionPane.showMessageDialog(this, "Evaluation saved for " + submission.getName());
                submission.setScores(0, 0, 0, 0); // Reset scores after saving
                ((JTextField) rubric.getComponent(9)).setText(""); // Clear comments field
            } else {
                JOptionPane.showMessageDialog(this, "Please select a submission to evaluate.");
            }
        });

        // Refresh table data whenever this panel becomes visible
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                tableModel.setRowCount(0); // Clear previous data
                
                User currentUser = frame.getCurrentUser();
                if (currentUser == null) return;

                Set<String> assignedSubmissionIds = new HashSet<>();
                List<Session> sessions = frame.getCoordinatorController().getAllSessions();
                
                for (Session session : sessions) {
                    List<String> evalIds = session.getAssignedEvaluatorIds();
                    if (evalIds != null && evalIds.contains(currentUser.getId())) {
                        List<String> subIds = session.getAssignedSubmissionIds();
                        if (subIds != null) {
                            assignedSubmissionIds.addAll(subIds);
                        }
                    }
                }

                for (Submission s : frame.getSubmissions()) {
                    if (assignedSubmissionIds.contains(s.getId())) {
                        tableModel.addRow(new Object[]{
                            s.getName(),
                            s.getTitle(),
                            s.getAbstractText(),
                            s.getPresentationType(),
                            s.getFilePath(),
                            s.getStatus(),
                            "View File"
                        });
                    }
                }
            }
        });


    }
}