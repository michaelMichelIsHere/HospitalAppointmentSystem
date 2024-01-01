package ui.gui;

import model.AppointmentSystem;
import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Represents the Patient Panel
public class PatientPanel extends JFrame {

    private AppointmentSystem appointmentSystem;

    // EFFECTS: creates the patient panel
    public PatientPanel(AppointmentSystem appointmentSystem) {
        this.appointmentSystem = appointmentSystem;
        initialize();
    }

    // EFFECTS: Initialize the GUI components for the staff panel
    private void initialize() {
        setWindowProperties();
        setUpWindowListener();

        JPanel patientPanel = new JPanel();
        patientPanel.setLayout(new GridLayout(4, 1));

        addButton(patientPanel, "Book Appointment", this::runBookAppointment);
        addButton(patientPanel, "Cancel Appointment", this::runCancelAppointment);
        addButton(patientPanel, "Cancel Waitlist", this::runCancelWaitlist);
        addButton(patientPanel, "View Waitlist Position", this::runViewWaitlistPosition);
        addButton(patientPanel, "Back to Main Panel", this::runBackToMainPanel);

        add(patientPanel);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets the window property
    private void setWindowProperties() {
        setTitle("Patient Panel");
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

    // EFFECTS: back to the main panel
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
