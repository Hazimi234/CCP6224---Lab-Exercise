package view;

import javax.swing.*;
import java.awt.*;

public class CoordinatorPanel extends JPanel {
    public CoordinatorPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: Management
        JPanel managePanel = new JPanel();
        managePanel.add(new JButton("Create New Session"));
        managePanel.add(new JButton("Assign Evaluators"));
        tabs.addTab("Assignments", managePanel);

        // Tab 2: Reports
        JPanel reportPanel = new JPanel();
        reportPanel.add(new JButton("Generate PDF Report"));
        reportPanel.add(new JButton("Compute Awards"));
        tabs.addTab("Reports & Awards", reportPanel);

        JButton backBtn = new JButton("Logout");
        
        add(new JLabel("Coordinator Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> frame.showScreen("Login"));
    }
}