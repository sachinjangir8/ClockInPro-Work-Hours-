package com.clockinpro.dao;

import com.clockinpro.model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO {
    
    public boolean registerEmployee(Employee employee) {
        String query = "INSERT INTO employees (name, email, password, hourly_rate, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, employee.getPassword());
            pstmt.setDouble(4, employee.getHourlyRate());
            pstmt.setString(5, employee.getRole() != null ? employee.getRole() : "EMPLOYEE");
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error registering employee: " + e.getMessage());
            return false;
        }
    }
    
    public Employee login(String email, String password) {
        String query = "SELECT * FROM employees WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setPassword(rs.getString("password"));
                emp.setHourlyRate(rs.getDouble("hourly_rate"));
                emp.setRole(rs.getString("role"));
                return emp;
            }
            
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;
    }
    
    public Employee getEmployeeById(int id) {
        String query = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setPassword(rs.getString("password"));
                emp.setHourlyRate(rs.getDouble("hourly_rate"));
                emp.setRole(rs.getString("role"));
                return emp;
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching employee: " + e.getMessage());
        }
        return null;
    }

    public java.util.List<Employee> getAllEmployees() {
        java.util.List<Employee> list = new java.util.ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setHourlyRate(rs.getDouble("hourly_rate"));
                emp.setRole(rs.getString("role"));
                list.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all employees: " + e.getMessage());
        }
        return list;
    }
}
