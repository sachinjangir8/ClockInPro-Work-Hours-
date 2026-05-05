USE clockinpro;
ALTER TABLE employees ADD COLUMN role VARCHAR(20) DEFAULT 'EMPLOYEE';
INSERT INTO employees (name, email, password, hourly_rate, role) VALUES ('System Admin', 'admin@clockinpro.com', 'admin123', 0.00, 'ADMIN');
