import java.io.*;
import java.util.*;

// ---------- Part B: Student Class for Serialization ----------
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private int studentID;
    private String name;
    private String grade;

    public Student(int studentID, String name, String grade) {
        this.studentID = studentID;
        this.name = name;
        this.grade = grade;
    }

    public void display() {
        System.out.println("Student ID: " + studentID);
        System.out.println("Name: " + name);
        System.out.println("Grade: " + grade);
    }
}

// ---------- Part C: Employee Class for File Handling ----------
class Employee {
    private String name;
    private int id;
    private String designation;
    private double salary;

    public Employee(String name, int id, String designation, double salary) {
        this.name = name;
        this.id = id;
        this.designation = designation;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + designation + "," + salary;
    }

    public String displayFormat() {
        return String.format("ID: %d | Name: %s | Designation: %s | Salary: %.2f",
                id, name, designation, salary);
    }

    public static Employee fromString(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        String designation = parts[2];
        double salary = Double.parseDouble(parts[3]);
        return new Employee(name, id, designation, salary);
    }
}

// ---------- Main Class ----------
public class CombinedDemo {
    private static final String EMP_FILE = "employees.txt";
    private static final String STUD_FILE = "student.ser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Combined Java Program =====");
            System.out.println("1. Part A: Sum of Integers (Autoboxing/Unboxing)");
            System.out.println("2. Part B: Student Serialization & Deserialization");
            System.out.println("3. Part C: Employee Management System (File Handling)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    partA_SumOfIntegers(sc);
                    break;
                case 2:
                    partB_StudentSerialization(sc);
                    break;
                case 3:
                    partC_EmployeeManagement(sc);
                    break;
                case 4:
                    System.out.println("Exiting the application. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ---------- Part A ----------
    private static void partA_SumOfIntegers(Scanner sc) {
        System.out.print("Enter integers separated by spaces: ");
        String input = sc.nextLine();
        String[] tokens = input.split("\\s+");

        ArrayList<Integer> numbers = new ArrayList<>();
        for (String token : tokens) {
            Integer num = Integer.parseInt(token); // parse string -> int -> autobox to Integer
            numbers.add(num);
        }

        int sum = 0;
        for (Integer n : numbers) { // unboxing occurs here
            sum += n;
        }

        System.out.println("Total Sum: " + sum);
    }

    // ---------- Part B ----------
    private static void partB_StudentSerialization(Scanner sc) {
        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Grade: ");
        String grade = sc.nextLine();

        Student s1 = new Student(id, name, grade);

        // Serialization
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STUD_FILE))) {
            out.writeObject(s1);
            System.out.println("Student object serialized to " + STUD_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialization
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STUD_FILE))) {
            Student s2 = (Student) in.readObject();
            System.out.println("\nDeserialized Student Details:");
            s2.display();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // ---------- Part C ----------
    private static void partC_EmployeeManagement(Scanner sc) {
        while (true) {
            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Add an Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addEmployee(sc);
                    break;
                case 2:
                    displayEmployees();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addEmployee(Scanner sc) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE, true))) {
            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Designation: ");
            String designation = sc.nextLine();
            System.out.print("Enter Salary: ");
            double salary = sc.nextDouble();
            sc.nextLine();

            Employee emp = new Employee(name, id, designation, salary);
            bw.write(emp.toString());
            bw.newLine();
            System.out.println("Employee added successfully!");
        } catch (IOException e) {
            System.out.println("Error while writing to file: " + e.getMessage());
        }
    }

    private static void displayEmployees() {
        File file = new File(EMP_FILE);
        if (!file.exists()) {
            System.out.println("No employee records found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            String line;
            boolean found = false;
            System.out.println("\n--- Employee Records ---");
            while ((line = br.readLine()) != null) {
                Employee emp = Employee.fromString(line);
                System.out.println(emp.displayFormat());
                found = true;
            }
            if (!found) {
                System.out.println("No employee records found.");
            }
        } catch (IOException e) {
            System.out.println("Error while reading from file: " + e.getMessage());
        }
    }
}
