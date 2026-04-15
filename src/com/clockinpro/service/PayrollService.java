package com.clockinpro.service;

import com.clockinpro.dao.AttendanceDAO;
import com.clockinpro.dao.EmployeeDAO;
import com.clockinpro.dao.PayrollDAO;
import com.clockinpro.model.Employee;
import com.clockinpro.model.Payroll;
import java.util.List;

public class PayrollService {
    private AttendanceDAO attendanceDAO;
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;

    public PayrollService() {
        this.attendanceDAO = new AttendanceDAO();
        this.payrollDAO = new PayrollDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    public void generatePayroll(int employeeId, String monthYear) {
        double totalHours = attendanceDAO.getTotalHoursByMonth(employeeId, monthYear);
        if (totalHours <= 0) {
            System.out.println("No completed work hours found for the month of " + monthYear);
            return;
        }

        Employee emp = employeeDAO.getEmployeeById(employeeId);
        double totalSalary = totalHours * emp.getHourlyRate();

        Payroll payroll = new Payroll();
        payroll.setEmployeeId(employeeId);
        payroll.setMonth(monthYear);
        payroll.setTotalHours(totalHours);
        payroll.setTotalSalary(totalSalary);

        boolean success = payrollDAO.savePayroll(payroll);
        if (success) {
            System.out.printf("Payroll generated successfully for %s! Total Hours: %.2f, Total Salary: $%.2f%n", 
                              monthYear, totalHours, totalSalary);
        } else {
            System.out.println("Failed to generate payroll.");
        }
    }

    public void displayMonthlyReports(int employeeId) {
        List<Payroll> reports = payrollDAO.getPayrollByEmployee(employeeId);
        if (reports.isEmpty()) {
            System.out.println("No payroll reports found.");
            return;
        }

        System.out.println("\n--- Monthly Payroll Reports ---");
        System.out.printf("%-10s | %-15s | %-15s%n", "Month", "Total Hours", "Total Salary");
        System.out.println("---------------------------------------------------");
        for (Payroll p : reports) {
            System.out.printf("%-10s | %-15.2f | $%-14.2f%n", p.getMonth(), p.getTotalHours(), p.getTotalSalary());
        }
        System.out.println("---------------------------------------------------");
    }
}
