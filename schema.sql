CREATE DATABASE IF NOT EXISTS clockinpro;
USE clockinpro;

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    hourly_rate DECIMAL(10, 2) NOT NULL
);

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    login_time DATETIME NOT NULL,
    logout_time DATETIME,
    total_hours DECIMAL(10, 2),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE TABLE payroll (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    month VARCHAR(7) NOT NULL, -- Format: YYYY-MM
    total_hours DECIMAL(10, 2) NOT NULL,
    total_salary DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    UNIQUE KEY (employee_id, month)
);

-- Sample Data
INSERT INTO employees (name, email, password, hourly_rate) VALUES 
('Alice Smith', 'alice@example.com', 'password123', 25.00),
('Bob Jones', 'bob@example.com', 'password123', 20.00);
