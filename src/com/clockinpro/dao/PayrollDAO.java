package com.clockinpro.dao;

import com.clockinpro.model.Payroll;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {
    
    public boolean savePayroll(Payroll payroll) {
        String checkQuery = "SELECT id FROM payroll WHERE employee_id = ? AND month = ?";
        String insertQuery = "INSERT INTO payroll (employee_id, month, total_hours, total_salary) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE payroll SET total_hours = ?, total_salary = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            
            checkStmt.setInt(1, payroll.getEmployeeId());
            checkStmt.setString(2, payroll.getMonth());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Update existing payroll record for this month
                int id = rs.getInt("id");
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setDouble(1, payroll.getTotalHours());
                    updateStmt.setDouble(2, payroll.getTotalSalary());
                    updateStmt.setInt(3, id);
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // Insert new payroll record
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, payroll.getEmployeeId());
                    insertStmt.setString(2, payroll.getMonth());
                    insertStmt.setDouble(3, payroll.getTotalHours());
                    insertStmt.setDouble(4, payroll.getTotalSalary());
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving payroll: " + e.getMessage());
            return false;
        }
    }
    
    public List<Payroll> getPayrollByEmployee(int employeeId) {
        List<Payroll> list = new ArrayList<>();
        String query = "SELECT * FROM payroll WHERE employee_id = ? ORDER BY month DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Payroll p = new Payroll();
                p.setId(rs.getInt("id"));
                p.setEmployeeId(rs.getInt("employee_id"));
                p.setMonth(rs.getString("month"));
                p.setTotalHours(rs.getDouble("total_hours"));
                p.setTotalSalary(rs.getDouble("total_salary"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching payroll details: " + e.getMessage());
        }
        return list;
    }
    
    public List<Payroll> getAllPayrolls() {
        List<Payroll> list = new ArrayList<>();
        String query = "SELECT * FROM payroll ORDER BY month DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Payroll p = new Payroll();
                p.setId(rs.getInt("id"));
                p.setEmployeeId(rs.getInt("employee_id"));
                p.setMonth(rs.getString("month"));
                p.setTotalHours(rs.getDouble("total_hours"));
                p.setTotalSalary(rs.getDouble("total_salary"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all payrolls: " + e.getMessage());
        }
        return list;
    }
}
