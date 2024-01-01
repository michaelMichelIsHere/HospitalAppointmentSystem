package ui;

import model.AppointmentSystem;
import model.Patient;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Represents a console-based Appointment system for patients and staff
// Saving and loading code influenced by JsonSerializationDemo https://github.com/stleary/JSON-java
public class AppointmentSystemApp {
    private static final String JSON_STORE = "./data/myFile.json";
    private Scanner input;
    private int studentNum;
    private AppointmentSystem appointmentSystem;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: construct the appointment system and runs the application
    public AppointmentSystemApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        appointmentSystem = new AppointmentSystem("UBC Student Hospital");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runPatientAndStaff();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runPatientAndStaff() {
        boolean keepRun = true;
        int command;

        while (keepRun) {
            showMenu();
            command = input.nextInt();

            if (command == 3) {
                keepRun = false;
            } else {
                operateCommand(command);
            }
        }

        System.out.println("\nBye Bye");
    }

    // MODIFIES: this
    // EFFECTS: select user type
    private void showMenu() {
        System.out.println("\nPlease select from the following options");
        System.out.println("\tPress 1 if you are a patient");
        System.out.println("\tPress 2 if you are a staff member");
        System.out.println("\tPress 3 if you want to exit");
        System.out.println("\tPress 4 if you want to save appointment system to file");
        System.out.println("\tPress 5 if you want to load appointment system to file");
    }

    // EFFECTS: processes user command
    private void operateCommand(int command) {
        if (command == 1) {
            runPatient();
        } else if (command == 2) {
            runStaff();
        } else if (command == 4) {
            saveAppointmentSystem();
        } else if (command == 5) {
            loadAppointmentSystem();
        } else {
            System.out.println("\nOops, invalid selection, please try again");
        }
    }

    // MODIFIES: this
    // EFFECTS: runs the operations as a patient
    public void runPatient() {
        showPatientMenu();
        int operationType = input.nextInt();

        if (operationType == 1) {
            runBookAppointment();
        } else if (operationType == 2) {
            runCancelAppointment();
        } else if (operationType == 3) {
            runCancelWaitlist();
        } else if (operationType == 4) {
            System.out.println("Please type the 8 digit student number");
            runViewWaitlistPosition(input.nextInt());
        } else {
            System.out.println("Invalid selection. Please try again.");
            runPatient();
        }
    }

    // EFFECTS: runs the patient menu
    public void showPatientMenu() {
        System.out.println("\tPress 1 to book an appointment");
        System.out.println("\tpress 2 to cancel an appointment");
        System.out.println("\tPress 3 to cancel a wait-list position");
        System.out.println("\tPress 4 to view wait list position");
    }

    // MODIFIES: this
    // EFFECTS: runs the operations as a staff
    public void runStaff() {
        runStaffMenu();
        int operationType = input.nextInt();

        if (operationType == 1) {
            runBookAppointment();
        } else if (operationType == 2) {
            runCancelAppointment();
        } else if (operationType == 3) {
            runCancelWaitlist();
        } else if (operationType == 4) {
            System.out.println("Please type the 8 digit student number");
            runViewAppointmentTime(input.nextInt());
        } else if (operationType == 5) {
            System.out.println("Please type the 8 digit student number");
            runViewWaitlistPosition(input.nextInt());
        } else if (operationType == 6) {
            runViewList();
        } else {
            System.out.println("Invalid selection. Please try again.");
            runStaff();
        }
    }

    // EFFECTS: runs the staff menu
    public void runStaffMenu() {
        System.out.println("\tPress 1 to book an appointment");
        System.out.println("\tpress 2 to cancel an appointment");
        System.out.println("\tPress 3 to cancel a student from wait-list");
        System.out.println("\tPress 4 if you want to view a student from the current appointment system");
        System.out.println("\tPress 5 if you want to view a student from the current wait list");
        System.out.println("\tPress 6 if you want to view a list of students in the appointment system");
    }

    // MODIFIES: this
    // EFFECTS: book appointment for patients
    public void runBookAppointment() {
        System.out.println("Please type your 8 digit student number");

        studentNum = input.nextInt();
        input.nextLine();
        Boolean success = appointmentSystem.bookAppointment(studentNum);
        if (success) {
            System.out.println("Sorry, the appointment system is full, but you are added to the waitlist");
            System.out.println("Press yes if you want to view your waitlist position");
            System.out.println("press no if you want to exit");

            String in = input.nextLine();

            if (in.equals("yes")) {
                runViewWaitlistPosition(studentNum);
            }
            if (in.equals("no")) {
                System.out.println("bye");
            }
        } else {
            System.out.println("You have been booked at: " + appointmentSystem.getAppointmentTime(studentNum)
                    + ":00");
        }
    }

    // MODIFIES: this
    // EFFECTS: cancels appointment for patients
    public void runCancelAppointment() {
        System.out.println("Please type your 8 digit student number");

        studentNum = input.nextInt();
        boolean success = appointmentSystem.cancelAppointment(studentNum);

        if (success) {
            System.out.println("Successfully removed patient from appointment system");
        } else {
            System.out.println("Sorry, we cannot find this patient");
        }
    }

    // MODIFIES: this
    // EFFECTS: cancels wait list for patients
    public void runCancelWaitlist() {
        System.out.println("Please type your 8 digit student number");

        studentNum = input.nextInt();
        boolean success = appointmentSystem.cancelWaitlist(studentNum);

        if (success) {
            System.out.println("Successfully removed patient from waitlist");
        } else {
            System.out.println("Sorry, we cannot find this patient");
        }
    }

    // EFFECTS: view the appointment time of a patient
    public void runViewAppointmentTime(int studentNumber) {
        int time = appointmentSystem.getAppointmentTime(studentNumber);
        if (time == -1) {
            System.out.println("Sorry, we cannot find this patient.");
        } else {
            System.out.println("Time: " + time + ":00");
        }
    }

    // EFFECTS: view the wait list position of a patient
    public void runViewWaitlistPosition(int studentNumber) {
        int pos = appointmentSystem.getWaitlistPosition(studentNumber);
        if (pos == -1) {
            System.out.println("Sorry, we cannot find this patient.");
        } else {
            System.out.println("Your wait list position is at: " + pos);
        }
    }

    // EFFECTS: view list
    public void runViewList() {
        System.out.println("Select 1 to view the appointment system");
        System.out.println("Select 2 to view the waitlist");

        int in = input.nextInt();

        if (in == 1) {
            if (appointmentSystem.isAppointmentListEmpty()) {
                System.out.println("Sorry, appointment system is empty");
            } else {
                runViewAppointmentList();
            }
        } else if (in == 2) {
            if (appointmentSystem.isWaitlistEmpty()) {
                System.out.println("Sorry, waitlist is empty");
            } else {
                runViewWaitlist();
            }
        } else {
            System.out.println("Invalid selection. Please try again.");
            runViewList();
        }
    }

    // EFFECTS: view appointment system
    public void runViewAppointmentList() {
        List<Patient> appointmentList = appointmentSystem.getAppointmentList();
        for (Patient patient: appointmentList) {
            if (patient == null) {
                continue;
            }
            int studentNum = patient.getStudentNum();
            System.out.println("Student number: " + studentNum + " " + "Booking time: "
                    + appointmentSystem.getAppointmentTime(studentNum));
        }
    }

    // EFFECTS: view wait list
    public void runViewWaitlist() {
        List<Patient> wl = appointmentSystem.getWaitlist();
        int pos = 1;
        for (Patient p: wl) {
            System.out.println("Student number: " + p.getStudentNum() + " " + "Position: " + pos);
            pos++;
        }
    }

    // EFFECTS: save the appointment system to file
    private void saveAppointmentSystem() {
        try {
            jsonWriter.open();
            jsonWriter.write(appointmentSystem);
            jsonWriter.close();
            System.out.println("Saved " + appointmentSystem.getHospitalName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads appointment system from file
    private void loadAppointmentSystem() {
        try {
            appointmentSystem = jsonReader.read();
            System.out.println("Loaded " + appointmentSystem.getHospitalName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
