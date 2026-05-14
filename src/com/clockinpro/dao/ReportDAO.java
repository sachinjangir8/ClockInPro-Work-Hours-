package com.clockinpro.dao;

import com.clockinpro.model.Report;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public boolean submitReport(int employeeId, String text) {
        String query = "INSERT INTO reports (employee_id, report_text) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, text);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error submitting report: " + e.getMessage());
            return false;
        }
    }

    public List<Report> getAllReports() {
        List<Report> list = new ArrayList<>();
        String query = "SELECT * FROM reports ORDER BY submitted_at DESC";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return list;
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    Report r = new Report();
                    r.setId(rs.getInt("id"));
                    r.setEmployeeId(rs.getInt("employee_id"));
                    r.setReportText(rs.getString("report_text"));
                    r.setSubmittedAt(rs.getTimestamp("submitted_at"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reports: " + e.getMessage());
        }
        return list;
    }
}
