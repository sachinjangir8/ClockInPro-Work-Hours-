package com.clockinpro.ui;

import com.clockinpro.model.Employee;
import com.clockinpro.service.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private EmployeeService employeeService;
    private Employee currentEmployee;

    private JLabel welcomeLabel;
    private JTable employeeTable;

    public AdminDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.employeeService = new EmployeeService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Admin Dashboard", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            this.currentEmployee = null;
            mainFrame.showPanel("Login");
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center configuration
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        JLabel subtitle = new JLabel("All Employees");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        contentPanel.add(subtitle, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Email", "Hourly Rate", "Role"};
        employeeTable = new JTable(new DefaultTableModel(cols, 0));
        contentPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> refreshData());
        contentPanel.add(refreshButton, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    public void setCurrentEmployee(Employee emp) {
        this.currentEmployee = emp;
        welcomeLabel.setText("Admin Dashboard - " + emp.getName());
        refreshData();
    }

    public void refreshData() {
        if (currentEmployee == null) return;

        List<Employee> employees = employeeService.getAllEmployees();
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        model.setRowCount(0);

        for (Employee e : employees) {
            model.addRow(new Object[]{
                e.getId(),
                e.getName(),
                e.getEmail(),
                String.format("$%.2f", e.getHourlyRate()),
                e.getRole()
            });
        }
    }
}
