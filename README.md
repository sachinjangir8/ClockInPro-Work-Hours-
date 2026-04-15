# ClockInPro (Work Hours & Payroll Tracker)

A Java console-based application to track employee work hours and calculate monthly payrolls. Built with Core Java, OOP principles, and JDBC (MySQL).

## Project Structure
- `src/com/clockinpro/model` - Contains Employee, Attendance, and Payroll entities.
- `src/com/clockinpro/dao` - Database Access Objects for DB logic and connection.
- `src/com/clockinpro/service` - Business logic and intermediate data handling.
- `src/com/clockinpro/main` - The main CLI application with user menus.

## Technologies Used
- Java 8+
- MySQL Server
- JDBC Driver (`mysql-connector-j`)

## Prerequisites
1. Ensure Java is installed (`java -version`).
2. Ensure MySQL server is installed and running (`mysql -u root -p`).
3. Download the MySQL JDBC Driver `.jar` file and include it in your classpath when running.

## Setup Instructions

### 1. Database Setup
1. Log into your MySQL Server.
2. Run the provided `schema.sql` script to create the `clockinpro` database and necessary tables.
   ```sql
   mysql -u root -p < schema.sql
   ```

### 2. Configure Database Credentials
By default, the application is configured to use `root` for both username and password. 
If your credentials differ, open `src/com/clockinpro/dao/DatabaseConnection.java` and update the constants:
```java
private static final String URL = "jdbc:mysql://localhost:3306/clockinpro";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

### 3. Run the Application
Compile and run the program using terminal or any IDE (like Eclipse/IntelliJ).

**From Command Line:**
*(Make sure to compile with the MySQL JDBC Connector in classpath)*
```sh
# Navigate to the project directory
cd ClockInPro

# Compile all classes directly using source path (make sure mysql jar is path)
javac -cp ".;path/to/mysql-connector-j.jar" src/com/clockinpro/main/ClockInProApp.java src/com/clockinpro/model/*.java src/com/clockinpro/dao/*.java src/com/clockinpro/service/*.java

# Run the project
java -cp "src;path/to/mysql-connector-j.jar" com.clockinpro.main.ClockInProApp
```
*(On MacOS/Linux, replace the semicolon `;` with a colon `:` in the classpath `-cp` arguments)*

## Features Overview
- **Register / Login**: Secure profile access.
- **Clock In / Out**: Accurate time tracking calculating actual worked hours.
- **Generate Payroll**: Calculate expected salary for a specific month based on logged hours and individual hourly rate.
- **View Reports**: Review historical work hours and finalized payrolls.
