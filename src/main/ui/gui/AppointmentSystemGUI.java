package ui.gui;

import model.AppointmentSystem;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents a Hospital Appointment System GUI

public class AppointmentSystemGUI extends JFrame {
    private static final String JSON_STORE = "./data/myFile.json";
    private AppointmentSystem appointmentSystem;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: creates AppointmentSystemGUI
    public AppointmentSystemGUI(AppointmentSystem appointmentSystem) {
        this.appointmentSystem = appointmentSystem;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        initialize();
    }

    // EFFECTS: Initialize the GUI components
    private void initialize() {
        setWindowProperties();
        setUpWindowListener();

        JPanel mainPanel = createMainPanel();

        addButtonsToMainPanel(mainPanel);

        add(mainPanel);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets the window property
    private void setWindowProperties() {
        setTitle("Appointment System");
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

    // MODIFIES: this
    // EFFECTS: creates the main panel with a background image
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon("./data/UBCH.jpg").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        mainPanel.setLayout(new GridLayout(3, 1));
        return mainPanel;
    }

    // MODIFIES: this
    // EFFECTS: adds 4 buttons to the main panel
    private void addButtonsToMainPanel(JPanel mainPanel) {
        addButtonToPanel(mainPanel, "Patient", this::openPatientPanel);
        addButtonToPanel(mainPanel, "Staff", this::openStaffPanel);
        addButtonToPanel(mainPanel, "Save", this::saveAppointmentSystem);
        addButtonToPanel(mainPanel, "Load", this::loadAppointmentSystem);
    }

    // EFFECTS: adds button to panel
    private void addButtonToPanel(JPanel panel, String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }

    // EFFECTS: Open the patient panel
    private void openPatientPanel(ActionEvent e) {
        PatientPanel patientPanel = new PatientPanel(appointmentSystem);
        setVisible(false);
        dispose();
    }

    // EFFECTS: Open the staff panel
    private void openStaffPanel(ActionEvent e) {
        StaffPanel staffPanel = new StaffPanel(appointmentSystem);
        setVisible(false);
        dispose();
    }

    // MODIFIES: this
    // EFFECTS: saves appointment system
    private void saveAppointmentSystem(ActionEvent e) {
        Boolean success = saveAppointmentSystemData();
        showResultDialog(success,
                "Saved " + appointmentSystem.getHospitalName() + " to " + JSON_STORE,
                "Unable to write to file: " + JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: loads the saved appointment system
    private void loadAppointmentSystem(ActionEvent e) {
        Boolean success = loadAppointmentSystemData();
        showResultDialog(success,
                "Loaded " + appointmentSystem.getHospitalName() + " from " + JSON_STORE,
                "Unable to read from file: " + JSON_STORE);
    }

    // EFFECTS: saves the appointment system data
    private Boolean saveAppointmentSystemData() {
        try {
            jsonWriter.open();
            jsonWriter.write(appointmentSystem);
            jsonWriter.close();
            return true;
        } catch (FileNotFoundException exception) {
            return false;
        }
    }

    // EFFECTS: loads the appointment system data
    private Boolean loadAppointmentSystemData() {
        try {
            appointmentSystem = jsonReader.read();
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    // EFFECTS: shows pop-up message
    private void showResultDialog(boolean condition, String successMessage, String failureMessage) {
        JOptionPane.showMessageDialog(null, condition ? successMessage : failureMessage);
    }


    // EFFECTS: opens a splash screen before the main panel opens
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splashScreen = new SplashScreen();

            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        splashScreen.dispose();
                        new AppointmentSystemGUI(new AppointmentSystem("UBC Hospital"));
                    });
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
}

