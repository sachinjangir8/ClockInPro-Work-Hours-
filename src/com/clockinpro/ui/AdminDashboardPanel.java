package com.clockinpro.ui;

import com.clockinpro.model.Employee;
import com.clockinpro.model.Payroll;
import com.clockinpro.service.EmployeeService;
import com.clockinpro.service.PayrollService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private EmployeeService employeeService;
    private PayrollService payrollService;
    private Employee currentEmployee;

    private JLabel welcomeLabel;
    private JTable employeeTable;
    private JTable payrollTable;
    private JLabel expensesLabel;

    public AdminDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.employeeService = new EmployeeService();
        this.payrollService = new PayrollService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 250));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        welcomeLabel = new JLabel("HR Dashboard", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(40, 40, 40));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            this.currentEmployee = null;
            mainFrame.showPanel("Login");
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // Tab 1: Employees List
        JPanel employeesPanel = new JPanel(new BorderLayout(10, 10));
        employeesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] empCols = {"ID", "Name", "Email", "Hourly Rate", "Role"};
        employeeTable = new JTable(new DefaultTableModel(empCols, 0));
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        employeeTable.setRowHeight(28);
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        employeesPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        tabbedPane.addTab("Employees List", employeesPanel);

        // Tab 2: Payroll Histories & Expenses
        JPanel payrollPanel = new JPanel(new BorderLayout(10, 10));
        payrollPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setOpaque(false);
        expensesLabel = new JLabel("Total Payroll Expenses: $0.00");
        expensesLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        expensesLabel.setForeground(new Color(0, 102, 204));
        statsPanel.add(expensesLabel);
        payrollPanel.add(statsPanel, BorderLayout.NORTH);

        String[] payCols = {"Payroll ID", "Employee ID", "Month", "Total Hours", "Total Salary"};
        payrollTable = new JTable(new DefaultTableModel(payCols, 0));
        payrollTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        payrollTable.setRowHeight(28);
        payrollTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        payrollPanel.add(new JScrollPane(payrollTable), BorderLayout.CENTER);
        tabbedPane.addTab("Payroll Histories", payrollPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom Action Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(51, 153, 255));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(150, 40));
        refreshButton.addActionListener(e -> refreshData());
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setCurrentEmployee(Employee emp) {
        this.currentEmployee = emp;
        welcomeLabel.setText("HR Dashboard - Welcome, " + emp.getName());
        refreshData();
    }

    public void refreshData() {
        if (currentEmployee == null) return;

        // Refresh Employees Table
        List<Employee> employees = employeeService.getAllEmployees();
        DefaultTableModel empModel = (DefaultTableModel) employeeTable.getModel();
        empModel.setRowCount(0);

        for (Employee e : employees) {
            empModel.addRow(new Object[]{
                e.getId(),
                e.getName(),
                e.getEmail(),
                String.format("$%.2f", e.getHourlyRate()),
                e.getRole()
            });
        }

        // Refresh Payroll Table & Calculate Expenses
        List<Payroll> payrolls = payrollService.getAllPayrolls();
        DefaultTableModel payModel = (DefaultTableModel) payrollTable.getModel();
        payModel.setRowCount(0);

        double totalExpenses = 0.0;
        for (Payroll p : payrolls) {
            payModel.addRow(new Object[]{
                p.getId(),
                p.getEmployeeId(),
                p.getMonth(),
                String.format("%.2f", p.getTotalHours()),
                String.format("$%.2f", p.getTotalSalary())
            });
            totalExpenses += p.getTotalSalary();
        }

        expensesLabel.setText(String.format("Total Payroll Expenses: $%.2f", totalExpenses));
    }
}
