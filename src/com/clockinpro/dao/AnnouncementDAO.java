package com.clockinpro.dao;

import com.clockinpro.model.Announcement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAO {

    public boolean createAnnouncement(String text) {
        String query = "INSERT INTO announcements (announcement_text) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, text);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error creating announcement: " + e.getMessage());
            return false;
        }
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        String query = "SELECT * FROM announcements ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return list;
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    Announcement a = new Announcement();
                    a.setId(rs.getInt("id"));
                    a.setAnnouncementText(rs.getString("announcement_text"));
                    a.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching announcements: " + e.getMessage());
        }
        return list;
    }
}
