package application.finalproject_ist311;

public class EmployeeRecords {

    private int employeeID;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeEmail;
    private double employeeSalary;
    private String employeeDepartment;

    public EmployeeRecords() { //FIXME: Add on as columns are added/changed
        this.employeeID = 0;
        this.employeeFirstName = "";
        this.employeeLastName = "";
        this.employeeEmail = "";
        this.employeeSalary = 0.00;
        this.employeeDepartment = "";
    }

    public EmployeeRecords(int id, String firstName, String lastName, String email, double salary, String department) { //FIXME: Add on as columns are added/changed
        this.employeeID = id;
        this.employeeFirstName = firstName;
        this.employeeLastName = lastName;
        this.employeeEmail = email;
        this.employeeSalary = salary;
        this.employeeDepartment = department;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(double salary) {this.employeeSalary = salary;}

    public String getEmployeeDepartment() { return employeeDepartment; }

    public void setEmployeeDepartment(String department) { this.employeeDepartment = department; }
}
