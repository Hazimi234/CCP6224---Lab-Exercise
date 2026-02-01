package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Session;
import models.Submission;
import models.User;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class CoordinatorPanel extends JPanel {
    private MainFrame frame;
    private DefaultTableModel sessionTableModel;
    private JComboBox<Session> sessionSelector;
    private JComboBox<Session> submissionSessionSelector;
    private JPanel evaluatorCheckboxesPanel;
    private JPanel submissionCheckboxesPanel;
    private JCheckBox filterByTypeBox;
    private JComboBox<Session> posterSessionSelector;
    private DefaultTableModel posterTableModel;

    public CoordinatorPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // --- Tab 1: Create Sessions ---
        JPanel managePanel = new JPanel();
        managePanel.setLayout(new BorderLayout());
        
        // Form
        JPanel createForm = new JPanel(new FlowLayout());
        JTextField dateField = new JTextField(8);
        JTextField timeField = new JTextField(5);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});
        JButton createBtn = new JButton("Create Session");
        JButton deleteBtn = new JButton("Delete Selected Session");
        
        createForm.add(new JLabel("Date (DD/MM):")); createForm.add(dateField);
        createForm.add(new JLabel("Time (HH:MM):")); createForm.add(timeField);
        createForm.add(new JLabel("Type:")); createForm.add(typeCombo);
        createForm.add(createBtn);
        createForm.add(deleteBtn);
        
        // Table
        String[] cols = {"ID", "Date", "Time", "Type"};
        sessionTableModel = new DefaultTableModel(cols, 0);
        JTable sessionTable = new JTable(sessionTableModel);
        
        managePanel.add(createForm, BorderLayout.NORTH);
        managePanel.add(new JScrollPane(sessionTable), BorderLayout.CENTER);

        tabs.addTab("Manage Sessions", managePanel);

        // --- Tab 2: Assign Evaluators ---
        JPanel assignPanel = new JPanel(new BorderLayout());
        JPanel topAssign = new JPanel(new FlowLayout());
        sessionSelector = new JComboBox<>();
        
        topAssign.add(new JLabel("Select Session:"));
        topAssign.add(sessionSelector);
        
        evaluatorCheckboxesPanel = new JPanel();
        evaluatorCheckboxesPanel.setLayout(new BoxLayout(evaluatorCheckboxesPanel, BoxLayout.Y_AXIS));
        JScrollPane checkboxScroll = new JScrollPane(evaluatorCheckboxesPanel);
        
        JButton saveAssignmentBtn = new JButton("Save Assignments");
        
        assignPanel.add(topAssign, BorderLayout.NORTH);
        assignPanel.add(checkboxScroll, BorderLayout.CENTER);
        assignPanel.add(saveAssignmentBtn, BorderLayout.SOUTH);
        
        tabs.addTab("Assign Evaluators", assignPanel);

        // --- Tab 3: Assign Submissions ---
        JPanel assignSubPanel = new JPanel(new BorderLayout());
        JPanel topAssignSub = new JPanel(new FlowLayout());
        submissionSessionSelector = new JComboBox<>();
        filterByTypeBox = new JCheckBox("Match Session Type", true);
        
        topAssignSub.add(new JLabel("Select Session:"));
        topAssignSub.add(submissionSessionSelector);
        topAssignSub.add(filterByTypeBox);
        
        submissionCheckboxesPanel = new JPanel();
        submissionCheckboxesPanel.setLayout(new BoxLayout(submissionCheckboxesPanel, BoxLayout.Y_AXIS));
        JScrollPane subCheckboxScroll = new JScrollPane(submissionCheckboxesPanel);
        
        JButton saveSubAssignmentBtn = new JButton("Save Assignments");
        
        assignSubPanel.add(topAssignSub, BorderLayout.NORTH);
        assignSubPanel.add(subCheckboxScroll, BorderLayout.CENTER);
        assignSubPanel.add(saveSubAssignmentBtn, BorderLayout.SOUTH);
        
        tabs.addTab("Assign Submissions", assignSubPanel);

        // --- Tab 4: Poster Presentation Management ---
        JPanel posterPanel = new JPanel(new BorderLayout());
        JPanel topPoster = new JPanel(new FlowLayout());
        posterSessionSelector = new JComboBox<>();
        
        topPoster.add(new JLabel("Select Poster Session:"));
        topPoster.add(posterSessionSelector);
        
        String[] posterCols = {"ID", "Student Name", "Project Title", "Abstract", "File Path", "Status", "Action"};
        posterTableModel = new DefaultTableModel(posterCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        JTable posterTable = new JTable(posterTableModel);
        posterTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        posterTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), posterTable));
        JScrollPane tableScroll = new JScrollPane(posterTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Assigned Poster Details"));
        
        posterPanel.add(topPoster, BorderLayout.NORTH);
        posterPanel.add(tableScroll, BorderLayout.CENTER);
        
        tabs.addTab("Poster Presentation Management", posterPanel);

        // --- Logic ---
        createBtn.addActionListener(e -> {
            try {
                frame.getCoordinatorController().createSession(
                    dateField.getText(), timeField.getText(), (String)typeCombo.getSelectedItem()
                );
                refreshData();
                dateField.setText(""); timeField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = sessionTable.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this session?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String id = (String) sessionTableModel.getValueAt(row, 0);
                    frame.getCoordinatorController().deleteSession(id);
                    refreshData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a session to delete.");
            }
        });

        // Auto-refresh listeners
        sessionSelector.addActionListener(e -> loadEvaluatorsForSession());
        submissionSessionSelector.addActionListener(e -> loadSubmissionsForSession());
        filterByTypeBox.addActionListener(e -> loadSubmissionsForSession());
        posterSessionSelector.addActionListener(e -> loadPosterData());
        
        saveAssignmentBtn.addActionListener(e -> {
            Session selected = (Session) sessionSelector.getSelectedItem();
            if (selected == null) return;
            
            List<String> selectedIds = new ArrayList<>();
            for (Component c : evaluatorCheckboxesPanel.getComponents()) {
                if (c instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox) c;
                    if (cb.isSelected()) {
                        selectedIds.add(cb.getActionCommand()); // We stored ID in ActionCommand
                    }
                }
            }
            
            frame.getCoordinatorController().updateSessionEvaluators(selected, selectedIds);
            JOptionPane.showMessageDialog(this, "Assignments Saved!");
        });

        saveSubAssignmentBtn.addActionListener(e -> {
            Session selected = (Session) submissionSessionSelector.getSelectedItem();
            if (selected == null) return;
            
            List<String> selectedIds = new ArrayList<>();
            for (Component c : submissionCheckboxesPanel.getComponents()) {
                if (c instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox) c;
                    if (cb.isSelected()) {
                        selectedIds.add(cb.getActionCommand());
                    }
                }
            }
            
            frame.getCoordinatorController().updateSessionSubmissions(selected, selectedIds);
            JOptionPane.showMessageDialog(this, "Submission Assignments Saved!");
        });

        JButton backBtn = new JButton("Logout");
        
        add(new JLabel("Coordinator Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> frame.showScreen("Login"));
        
        // Refresh when shown
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshData();
            }
        });
    }

    private void refreshData() {
        // Refresh Table
        sessionTableModel.setRowCount(0);
        sessionSelector.removeAllItems();
        submissionSessionSelector.removeAllItems();
        posterSessionSelector.removeAllItems();
        
        for (Session s : frame.getCoordinatorController().getAllSessions()) {
            sessionTableModel.addRow(new Object[]{s.getId(), s.getDate(), s.getTime(), s.getType()});
            sessionSelector.addItem(s);
            submissionSessionSelector.addItem(s);
            if ("Poster".equals(s.getType())) {
                posterSessionSelector.addItem(s);
            }
        }
    }

    private void loadEvaluatorsForSession() {
        evaluatorCheckboxesPanel.removeAll();
        Session selected = (Session) sessionSelector.getSelectedItem();
        if (selected == null) return;

        List<String> assignedIds = selected.getAssignedEvaluatorIds();
        List<User> evaluators = frame.getCoordinatorController().getAllEvaluators();

        for (User u : evaluators) {
            JCheckBox cb = new JCheckBox(u.getName() + " (" + u.getId() + ")");
            cb.setActionCommand(u.getId()); // Store ID to retrieve later
            if (assignedIds.contains(u.getId())) {
                cb.setSelected(true);
            }
            evaluatorCheckboxesPanel.add(cb);
        }
        evaluatorCheckboxesPanel.revalidate();
        evaluatorCheckboxesPanel.repaint();
    }

    private void loadSubmissionsForSession() {
        submissionCheckboxesPanel.removeAll();
        Session selected = (Session) submissionSessionSelector.getSelectedItem();
        if (selected == null) return;

        List<String> assignedIds = selected.getAssignedSubmissionIds();
        List<Submission> submissions = frame.getCoordinatorController().getAllSubmissions();
        boolean filter = filterByTypeBox.isSelected();
        String sessionType = selected.getType();

        for (Submission s : submissions) {
            boolean isAssigned = assignedIds != null && assignedIds.contains(s.getId());
            // Filter logic: If filter is ON, and it's NOT already assigned, and types don't match -> Skip
            if (filter && !isAssigned && (s.getPresentationType() == null || !s.getPresentationType().equals(sessionType))) {
                continue;
            }
            JCheckBox cb = new JCheckBox(s.getTitle() + " by " + s.getName());
            cb.setActionCommand(s.getId());
            if (isAssigned) {
                cb.setSelected(true);
            }
            submissionCheckboxesPanel.add(cb);
        }
        submissionCheckboxesPanel.revalidate();
        submissionCheckboxesPanel.repaint();
    }

    private void loadPosterData() {
        posterTableModel.setRowCount(0);
        Session selected = (Session) posterSessionSelector.getSelectedItem();
        if (selected == null) return;

        List<String> assignedIds = selected.getAssignedSubmissionIds();
        List<Submission> submissions = frame.getCoordinatorController().getAllSubmissions();

        for (Submission s : submissions) {
            if ("Poster".equals(s.getPresentationType())) {
                boolean isAssigned = assignedIds != null && assignedIds.contains(s.getId());
                if (isAssigned) {
                    posterTableModel.addRow(new Object[]{
                        s.getId(),
                        s.getName(),
                        s.getTitle(),
                        s.getAbstractText(),
                        s.getFilePath(),
                        s.getStatus(),
                        "View File"
                    });
                }
            }
        }
    }

    // --- Inner Classes for Table Button ---

    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
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
                // File Path is at index 4 based on posterCols definition
                Object val = table.getValueAt(row, 4); 
                String filePath = (val != null) ? val.toString() : "";
                openFile(filePath);
                fireEditingStopped();
            });
        }

        private void openFile(String filePath) {
            try {
                File file = new File(filePath);
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(null, "File not found: " + filePath);
                }
            } catch (IOException | SecurityException ex) {
                JOptionPane.showMessageDialog(null, "Error opening file: " + ex.getMessage());
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
}