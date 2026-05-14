package com.clockinpro.ui;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    private MainFrame mainFrame;

    public WelcomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 244, 248)); // Matches LoginPanel background

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 95, 200), 2),
                BorderFactory.createEmptyBorder(50, 60, 50, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Title
        JLabel titleLabel = new JLabel("Welcome to ClockInPro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 95, 200));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        contentPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Manage your workforce with precision and ease.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        contentPanel.add(subtitleLabel, gbc);

        // Description Area
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("ClockInPro is a comprehensive employee time tracking and payroll management system. " +
                "Designed for both employees and administrators, it streamlines the daily workflow by providing " +
                "real-time attendance tracking, detailed work hour logs, and automated payroll calculations.\n\n" +
                "Key Features:\n" +
                "• Seamless Clock-In/Out\n" +
                "• Real-time Attendance Monitoring\n" +
                "• Detailed Payroll Generation\n" +
                "• Secure Admin Controls");
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        contentPanel.add(descriptionArea, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton loginBtn = createStyledButton("Login", new Color(0, 95, 200));
        loginBtn.addActionListener(e -> mainFrame.showPanel("Login"));

        JButton registerBtn = createStyledButton("Register", new Color(40, 167, 69));
        registerBtn.addActionListener(e -> mainFrame.showPanel("Register"));

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(buttonPanel, gbc);

        add(contentPanel);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add simple hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
}
