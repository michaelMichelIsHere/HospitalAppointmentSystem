package ui.gui;

import model.AppointmentSystem;
import model.Event;
import model.EventLog;
import model.Patient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the Staff Panel
public class StaffPanel extends JFrame {

    private AppointmentSystem appointmentSystem;

    // EFFECTS: Creates the staff panel
    public StaffPanel(AppointmentSystem appointmentSystem) {
        this.appointmentSystem = appointmentSystem;
        initialize();
    }

    // EFFECTS: Initialize the GUI components for the staff panel
    private void initialize() {
        setWindowProperties();
        setUpWindowListener();

        JPanel staffPanel = new JPanel();
        staffPanel.setLayout(new GridLayout(6, 1));

        addButton(staffPanel, "Book Appointment", this::runBookAppointment);
        addButton(staffPanel, "Cancel Appointment", this::runCancelAppointment);
        addButton(staffPanel, "Cancel Waitlist", this::runCancelWaitlist);
        addButton(staffPanel, "View Appointment Time", this::runViewAppointmentTime);
        addButton(staffPanel, "View Waitlist Position", this::runViewWaitlistPosition);
        addButton(staffPanel, "View Whole Lists", this::runViewList);
        addButton(staffPanel, "Back to Main Panel", this::runBackToMainPanel);

        add(staffPanel);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets the window property
    private void setWindowProperties() {
        setTitle("Staff Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
    }

    // EFFECTS: sets up the window listener
    private void setUpWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    // EFFECTS: handles window closing
    private void handleWindowClosing() {
        printEventsOnExit();
        dispose();
    }

    // EFFECTS: print events on exit
    private void printEventsOnExit() {
        EventLog eventLog = EventLog.getInstance();
        System.out.println("Events logged:");

        for (Event event : eventLog) {
            System.out.println(event.toString());
        }

        System.out.println("End of events");
    }

    // EFFECTS: adds button to the panel
    private void addButton(JPanel panel, String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }

    // MODIFIES: this
    // EFFECTS: book appointment for patients
    private void runBookAppointment(ActionEvent e) {
        int studentNum = getStudentNumber();
        if (studentNum == -1) {
            JOptionPane.showMessageDialog(null, "Invalid input");
            return;
        }

        Boolean ifFull = appointmentSystem.bookAppointment(studentNum);

        if (ifFull) {
            showWaitlistDialog(studentNum);
        } else {
            showBookedDialog(studentNum);
        }
    }

    // MODIFIES: this
    // EFFECTS: cancels appointment for patients
    private void runCancelAppointment(ActionEvent e) {
        int studentNum = getStudentNumber();
        if (studentNum == -1) {
            return;
        }

        boolean success = appointmentSystem.cancelAppointment(studentNum);

        showResultDialog(success, "Successfully removed patient from the appointment system.",
                "Sorry, we cannot find this patient.");
    }

    // MODIFIES: this
    // EFFECTS: cancels wait list for patients
    private void runCancelWaitlist(ActionEvent e) {
        int studentNum = getStudentNumber();
        if (studentNum == -1) {
            return;
        }

        boolean success = appointmentSystem.cancelWaitlist(studentNum);

        showResultDialog(success, "Successfully removed patient from the waitlist.",
                "Sorry, we cannot find this patient in the waitlist.");
    }

    // EFFECTS: view the appointment time of a patient
    private void runViewAppointmentTime(ActionEvent e) {
        int studentNum = getStudentNumber();
        if (studentNum == -1) {
            return;
        }

        int appointmentTime = appointmentSystem.getAppointmentTime(studentNum);
        showResultDialog(appointmentTime != -1,
                "Appointment time for student " + studentNum + " is at: " + appointmentTime + ":00",
                "Sorry, we cannot find this patient.");
    }

    // EFFECTS: view the wait list position of a patient
    private void runViewWaitlistPosition(ActionEvent e) {
        int studentNum = getStudentNumber();
        if (studentNum == -1) {
            return;
        }

        int waitlistPosition = appointmentSystem.getWaitlistPosition(studentNum);

        showResultDialog(waitlistPosition != -1,
                "Your waitlist position is at: " + waitlistPosition,
                "Sorry, we cannot find this patient.");
    }

    // EFFECTS: view lists of students in the current appointment and wait list
    private void runViewList(ActionEvent e) {
        runViewAppointmentList();
        runViewWaitlist();
    }

    // EFFECTS: view the appointment list
    private void runViewAppointmentList() {
        List<Patient> appointmentList = appointmentSystem.getAppointmentList();
        StringBuilder message = new StringBuilder("Current Appointment List:\n");
        if (appointmentList.isEmpty()) {
            message.append("The appointment list is empty.");
        } else {
            for (Patient patient : appointmentList) {
                if (patient == null) {
                    continue;
                }
                int studentNum = patient.getStudentNum();
                message.append("Student number: ").append(studentNum).append(" Appointment time: ")
                        .append(appointmentSystem.getAppointmentTime(studentNum)).append(":00\n");
            }
        }
        showResultDialog(!appointmentList.isEmpty(), message.toString(), "Empty Appointment List");
    }

    // EFFECTS: view the waitlist
    private void runViewWaitlist() {
        List<Patient> waitlist = appointmentSystem.getWaitlist();
        StringBuilder message = new StringBuilder("Current Waitlist:\n");
        if (waitlist.isEmpty()) {
            message.append("The waitlist is empty.");
        } else {
            for (Patient patient : waitlist) {
                message.append("Student number: ").append(patient.getStudentNum()).append("\n");
            }
        }
        showResultDialog(!waitlist.isEmpty(), message.toString(), "Empty Wait List");
    }

    // EFFECTS: go back to the main panel
    private void runBackToMainPanel(ActionEvent e) {
        AppointmentSystemGUI mainPanel = new AppointmentSystemGUI(appointmentSystem);
        mainPanel.setVisible(true);
        this.dispose();
    }

    // EFFECTS: returns student number, returns -1 if it is null or non-digit
    private int getStudentNumber() {
        String studentNumStr = JOptionPane.showInputDialog("Enter your 8-digit student number:");
        try {
            return (studentNumStr == null) ? -1 : Integer.parseInt(studentNumStr);
        } catch (NumberFormatException e) {
            // Handle invalid input (non-numeric)
            return -1;
        }
    }

    // EFFECTS: shows patients that they have been added to the wait list
    private void showWaitlistDialog(int studentNum) {
        JOptionPane.showMessageDialog(null, "Sorry, the appointment system is full, "
                + "but you are added to the waitlist");
    }

    // EFFECTS: shows patients that they have been added to the appointment list
    private void showBookedDialog(int studentNum) {
        int appointmentTime = appointmentSystem.getAppointmentTime(studentNum);
        JOptionPane.showMessageDialog(null, "You have been booked at: " + appointmentTime + ":00");
    }

    // EFFECTS: shows the pop-up message
    private void showResultDialog(boolean condition, String successMessage, String failureMessage) {
        JOptionPane.showMessageDialog(null, condition ? successMessage : failureMessage);
    }

}

