import java.sql.*;
import java.util.*;

class Patient {
    int patientID;
    String name;
    int age;
    String disease;

    public Patient(int patientID, String name, int age, String disease) {
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.disease = disease;
    }

    public void displayPatientDetails() {
        System.out.println("Patient ID: " + patientID);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Disease: " + disease);
    }
}

class Appointment {
    int appointmentID;
    int patientID;
    java.util.Date appointmentDate; // Use java.util.Date explicitly
    String doctorAssigned;

    public Appointment(int appointmentID, int patientID, java.util.Date appointmentDate, String doctorAssigned) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.appointmentDate = appointmentDate;
        this.doctorAssigned = doctorAssigned;
    }

    public void displayAppointmentDetails() {
        System.out.println("Appointment ID: " + appointmentID);
        System.out.println("Patient ID: " + patientID);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Doctor Assigned: " + doctorAssigned);
    }
}

class Ward {
    int wardNumber;
    int capacity;

    public Ward(int wardNumber, int capacity) {
        this.wardNumber = wardNumber;
        this.capacity = capacity;
    }
}

class Bill {
    int patientID;
    double totalCost;
    String servicesProvided;

    public Bill(int patientID, double totalCost, String servicesProvided) {
        this.patientID = patientID;
        this.totalCost = totalCost;
        this.servicesProvided = servicesProvided;
    }

    public void displayBillDetails() {
        System.out.println("Patient ID: " + patientID);
        System.out.println("Total Cost: " + totalCost);
        System.out.println("Services Provided: " + servicesProvided);
    }
}

public class V318050 {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Patient> patients = new ArrayList<>();
    private static List<Appointment> appointments = new ArrayList<>();
    private static List<Ward> wards = new ArrayList<>();
    private static List<Bill> bills = new ArrayList<>();

    // Database connection method
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3307/hospital", "root", "");
    }

    // Database method to add patient
    public static void addPatientToDB(Patient patient) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Patients (patient_id, name, age, disease) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, patient.patientID);
                stmt.setString(2, patient.name);
                stmt.setInt(3, patient.age);
                stmt.setString(4, patient.disease);
                stmt.executeUpdate();
                System.out.println("Patient added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add appointment to database
    public static void addAppointmentToDB(Appointment appointment) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Appointments (appointment_id, patient_id, appointment_date, doctor_assigned) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, appointment.appointmentID);
                stmt.setInt(2, appointment.patientID);
                stmt.setTimestamp(3, new Timestamp(appointment.appointmentDate.getTime())); // Convert to Timestamp
                stmt.setString(4, appointment.doctorAssigned);
                stmt.executeUpdate();
                System.out.println("Appointment added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add ward to database
    public static void addWardToDB(Ward ward) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Wards (ward_number, capacity) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, ward.wardNumber);
                stmt.setInt(2, ward.capacity);
                stmt.executeUpdate();
                System.out.println("Ward added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Generate bill and add to database
    public static void generateBillToDB(Bill bill) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Bills (patient_id, total_cost, services_provided) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, bill.patientID);
                stmt.setDouble(2, bill.totalCost);
                stmt.setString(3, bill.servicesProvided);
                stmt.executeUpdate();
                System.out.println("Bill generated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // List all patients
    public static void displayAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
        } else {
            for (Patient patient : patients) {
                patient.displayPatientDetails();
            }
        }
    }

    // Search patient by ID
    public static void searchPatientByID() {
        System.out.print("Enter Patient ID to search: ");
        int searchID = scanner.nextInt();
        Patient patient = getPatientByID(searchID);
        if (patient != null) {
            patient.displayPatientDetails();
        }
    }

    // Database method to fetch patient by ID
    public static Patient getPatientByID(int patientID) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM Patients WHERE patient_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, patientID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("disease")
                    );
                } else {
                    System.out.println("No patient found with ID " + patientID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Main logic for hospital management system
    public static void main(String[] args) {
        int choice;
        while (true) {
            System.out.println("\n--- Hospital Management System ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Display All Patients");
            System.out.println("3. Search Patient by ID");
            System.out.println("4. Add Appointment");
            System.out.println("5. Assign Ward");
            System.out.println("6. Generate Bill");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    displayAllPatients();
                    break;
                case 3:
                    searchPatientByID();
                    break;
                case 4:
                    addAppointment();
                    break;
                case 5:
                    assignWard();
                    break;
                case 6:
                    generateBill();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addPatient() {
        System.out.print("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Disease: ");
        String disease = scanner.nextLine();

        Patient patient = new Patient(patientID, name, age, disease);
        addPatientToDB(patient);
    }

    private static void addAppointment() {
        System.out.print("Enter Appointment ID: ");
        int appointmentID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        scanner.nextLine();

        // Continue from addAppointment method
        System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date appointmentDate = null;
        try {
            appointmentDate = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        System.out.print("Enter Doctor Assigned: ");
        String doctorAssigned = scanner.nextLine();

        Appointment appointment = new Appointment(appointmentID, patientID, appointmentDate, doctorAssigned);
        addAppointmentToDB(appointment);
    }

    private static void assignWard() {
        System.out.print("Enter Ward Number: ");
        int wardNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Ward Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Ward ward = new Ward(wardNumber, capacity);
        addWardToDB(ward);
    }

    private static void generateBill() {
        System.out.print("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Total Bill Cost: ");
        double totalCost = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Services Provided: ");
        String servicesProvided = scanner.nextLine();

        Bill bill = new Bill(patientID, totalCost, servicesProvided);
        generateBillToDB(bill);
    }
}