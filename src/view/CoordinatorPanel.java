package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Session;
import models.Submission;
import models.User;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorPanel extends JPanel {
    private MainFrame frame;
    private DefaultTableModel sessionTableModel;
    private JComboBox<Session> sessionSelector;
    private JComboBox<Session> submissionSessionSelector;
    private JPanel evaluatorCheckboxesPanel;
    private JPanel submissionCheckboxesPanel;
    private JCheckBox filterByTypeBox;

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
        
        for (Session s : frame.getCoordinatorController().getAllSessions()) {
            sessionTableModel.addRow(new Object[]{s.getId(), s.getDate(), s.getTime(), s.getType()});
            sessionSelector.addItem(s);
            submissionSessionSelector.addItem(s);
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
}