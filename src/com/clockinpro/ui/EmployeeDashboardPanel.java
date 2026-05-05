package com.clockinpro.ui;

import com.clockinpro.model.Attendance;
import com.clockinpro.model.Employee;
import com.clockinpro.model.Payroll;
import com.clockinpro.service.AttendanceService;
import com.clockinpro.service.PayrollService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeDashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private AttendanceService attendanceService;
    private PayrollService payrollService;
    private Employee currentEmployee;

    private JLabel welcomeLabel;
    private JTable hoursTable;
    private JTable payrollTable;

    public EmployeeDashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.attendanceService = new AttendanceService();
        this.payrollService = new PayrollService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Welcome!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            this.currentEmployee = null;
            mainFrame.showPanel("Login");
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center configuration (Tabs for different views)
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel 1: Actions & Hours
        JPanel attendancePanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton clockInButton = new JButton("Clock In");
        clockInButton.setBackground(new Color(40, 167, 69));
        clockInButton.setForeground(Color.WHITE);
        clockInButton.addActionListener(e -> {
            String msg = attendanceService.clockIn(currentEmployee.getId());
            JOptionPane.showMessageDialog(this, msg);
            refreshData();
        });

        JButton clockOutButton = new JButton("Clock Out");
        clockOutButton.setBackground(new Color(220, 53, 69));
        clockOutButton.setForeground(Color.WHITE);
        clockOutButton.addActionListener(e -> {
            String msg = attendanceService.clockOut(currentEmployee.getId());
            JOptionPane.showMessageDialog(this, msg);
            refreshData();
        });

        actionPanel.add(clockInButton);
        actionPanel.add(clockOutButton);
        attendancePanel.add(actionPanel, BorderLayout.NORTH);

        String[] hoursCols = {"Login Time", "Logout Time", "Hours"};
        hoursTable = new JTable(new DefaultTableModel(hoursCols, 0));
        attendancePanel.add(new JScrollPane(hoursTable), BorderLayout.CENTER);
        tabbedPane.addTab("Attendance & Hours", attendancePanel);

        // Panel 2: Payroll
        JPanel payrollPanel = new JPanel(new BorderLayout(10, 10));
        JPanel generatePanel = new JPanel(new FlowLayout());
        generatePanel.add(new JLabel("Month (YYYY-MM):"));
        JTextField monthField = new JTextField(10);
        generatePanel.add(monthField);
        JButton generateButton = new JButton("Generate Payroll");
        generateButton.addActionListener(e -> {
            String mo = monthField.getText().trim();
            if(!mo.matches("\\d{4}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Invalid format. Use YYYY-MM.");
                return;
            }
            String result = payrollService.generatePayroll(currentEmployee.getId(), mo);
            JOptionPane.showMessageDialog(this, result);
            refreshData();
        });
        generatePanel.add(generateButton);
        payrollPanel.add(generatePanel, BorderLayout.NORTH);

        String[] payCols = {"Month", "Total Hours", "Total Salary"};
        payrollTable = new JTable(new DefaultTableModel(payCols, 0));
        payrollPanel.add(new JScrollPane(payrollTable), BorderLayout.CENTER);
        tabbedPane.addTab("Payroll Reports", payrollPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void setCurrentEmployee(Employee emp) {
        this.currentEmployee = emp;
        welcomeLabel.setText("Welcome, " + emp.getName());
    }

    public void refreshData() {
        if (currentEmployee == null) return;

        // Refresh Hours Table
        List<Attendance> records = attendanceService.getWorkHoursHistory(currentEmployee.getId());
        DefaultTableModel hoursModel = (DefaultTableModel) hoursTable.getModel();
        hoursModel.setRowCount(0);
        for (Attendance att : records) {
            String logout = (att.getLogoutTime() != null) ? att.getLogoutTime().toString() : "Current";
            hoursModel.addRow(new Object[]{
                att.getLoginTime().toString(),
                logout,
                String.format("%.2f", att.getTotalHours())
            });
        }

        // Refresh Payroll Table
        List<Payroll> reports = payrollService.getMonthlyReports(currentEmployee.getId());
        DefaultTableModel payrollModel = (DefaultTableModel) payrollTable.getModel();
        payrollModel.setRowCount(0);
        for (Payroll p : reports) {
            payrollModel.addRow(new Object[]{
                p.getMonth(),
                String.format("%.2f", p.getTotalHours()),
                String.format("$%.2f", p.getTotalSalary())
            });
        }
    }
}
