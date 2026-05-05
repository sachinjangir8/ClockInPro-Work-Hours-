package com.clockinpro.ui;

import com.clockinpro.service.EmployeeService;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private EmployeeService employeeService;

    private JTextField nameField, emailField, rateField;
    private JPasswordField passwordField;

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.employeeService = new EmployeeService();

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; formPanel.add(new JLabel("Full Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Hourly Rate (e.g. 20.0):"), gbc);
        rateField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(rateField, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> attemptRegister());
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(registerButton, gbc);

        JButton backButton = new JButton("Already have an account? Login");
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(new Color(0, 102, 204));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> mainFrame.showPanel("Login"));
        gbc.gridy = 6;
        formPanel.add(backButton, gbc);

        add(formPanel);
    }

    private void attemptRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String rateStr = rateField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || rateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double rate;
        try {
            rate = Double.parseDouble(rateStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid hourly rate.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = employeeService.register(name, email, password, rate);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showPanel("Login");
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            rateField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Email might exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
