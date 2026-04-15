package com.clockinpro.main;

import com.clockinpro.model.Employee;
import com.clockinpro.service.AttendanceService;
import com.clockinpro.service.EmployeeService;
import com.clockinpro.service.PayrollService;
import java.util.Scanner;

public class ClockInProApp {
    private static EmployeeService empService = new EmployeeService();
    private static AttendanceService attService = new AttendanceService();
    private static PayrollService payrollService = new PayrollService();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  Welcome to ClockInPro System           ");
        System.out.println("  (Work Hours & Payroll Tracker)         ");
        System.out.println("=========================================");
        
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    registerMenu();
                    break;
                case "2":
                    loginMenu();
                    break;
                case "3":
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void registerMenu() {
        System.out.println("\n--- Employee Registration ---");
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter hourly rate (e.g. 25.50): ");
        double hourlyRate = 0;
        try {
            hourlyRate = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid rate format. Registration failed.");
            return;
        }
        
        boolean success = empService.register(name, email, password, hourlyRate);
        if (success) {
            System.out.println("Registration successful! You can now log in.");
        } else {
            System.out.println("Email might already be registered or invalid input.");
        }
    }
    
    private static void loginMenu() {
        System.out.println("\n--- Employee Login ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        Employee currentEmp = empService.login(email, password);
        if (currentEmp != null) {
            System.out.println("Login successful! Welcome, " + currentEmp.getName());
            dashboardMenu(currentEmp);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }
    
    private static void dashboardMenu(Employee employee) {
        while (true) {
            System.out.println("\n--- Employee Dashboard ---");
            System.out.println("1. Clock In (Start Work)");
            System.out.println("2. Clock Out (End Work)");
            System.out.println("3. View Work Hours History");
            System.out.println("4. Generate Payroll for a Month");
            System.out.println("5. View All Monthly Reports");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    attService.clockIn(employee.getId());
                    break;
                case "2":
                    attService.clockOut(employee.getId());
                    break;
                case "3":
                    attService.displayWorkHours(employee.getId());
                    break;
                case "4":
                    System.out.print("Enter month & year to generate (Format: YYYY-MM, e.g., 2023-10): ");
                    String monthYear = scanner.nextLine();
                    if(monthYear.matches("\\d{4}-\\d{2}")) {
                        payrollService.generatePayroll(employee.getId(), monthYear);
                    } else {
                        System.out.println("Invalid format. Use YYYY-MM.");
                    }
                    break;
                case "5":
                    payrollService.displayMonthlyReports(employee.getId());
                    break;
                case "6":
                    System.out.println("Logging out from dashboard...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
