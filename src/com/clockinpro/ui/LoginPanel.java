package com.clockinpro.ui;

import com.clockinpro.model.Employee;
import com.clockinpro.service.EmployeeService;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private EmployeeService employeeService;

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.employeeService = new EmployeeService();

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Light slate/blue

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome to ClockInPro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(51, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> attemptLogin());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(loginButton, gbc);

        JButton registerButton = new JButton("Don't have an account? Register");
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setForeground(new Color(0, 102, 204));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> mainFrame.showPanel("Register"));

        gbc.gridy = 4;
        formPanel.add(registerButton, gbc);

        add(formPanel);
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Employee emp = employeeService.login(email, password);
        if (emp != null) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            if ("ADMIN".equalsIgnoreCase(emp.getRole())) {
                if (mainFrame.getAdminDashboardPanel() != null) {
                    mainFrame.getAdminDashboardPanel().setCurrentEmployee(emp);
                    mainFrame.showPanel("AdminDashboard");
                } else {
                    JOptionPane.showMessageDialog(this, "Admin Dashboard not initialized yet.");
                }
            } else {
                mainFrame.getEmployeeDashboardPanel().setCurrentEmployee(emp);
                mainFrame.showPanel("EmployeeDashboard");
            }
            
            // clear fields
            emailField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
