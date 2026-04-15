package com.clockinpro.service;

import com.clockinpro.dao.AttendanceDAO;
import com.clockinpro.model.Attendance;
import java.sql.Timestamp;
import java.util.List;

public class AttendanceService {
    private AttendanceDAO attendanceDAO;

    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
    }

    public void clockIn(int employeeId) {
        Attendance current = attendanceDAO.getActiveLogin(employeeId);
        if (current != null) {
            System.out.println("Error: You are already clocked in! Please clock out first.");
            return;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        boolean success = attendanceDAO.recordLogin(employeeId, now);
        if (success) {
            System.out.println("Success: Clocked in at " + now);
        } else {
            System.out.println("Error during clock-in. Please try again.");
        }
    }

    public void clockOut(int employeeId) {
        Attendance current = attendanceDAO.getActiveLogin(employeeId);
        if (current == null) {
            System.out.println("Error: You have not clocked in yet!");
            return;
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        long msDiff = now.getTime() - current.getLoginTime().getTime();
        double hours = msDiff / (1000.0 * 60 * 60);

        boolean success = attendanceDAO.recordLogout(current.getId(), now, hours);
        if (success) {
            System.out.println(String.format("Success: Clocked out at %s. Session hours: %.2f", now, hours));
        } else {
            System.out.println("Error during clock-out. Please try again.");
        }
    }

    public void displayWorkHours(int employeeId) {
        List<Attendance> records = attendanceDAO.getAttendanceByEmployee(employeeId);
        if(records.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        }
        
        System.out.println("\n--- Work Hours History ---");
        System.out.printf("%-25s | %-25s | %-10s%n", "Login Time", "Logout Time", "Hours");
        System.out.println("----------------------------------------------------------------------");
        for (Attendance att : records) {
            String logout = att.getLogoutTime() != null ? att.getLogoutTime().toString() : "Currently Active";
            System.out.printf("%-25s | %-25s | %-10.2f%n", att.getLoginTime(), logout, att.getTotalHours());
        }
        System.out.println("----------------------------------------------------------------------");
    }
}
