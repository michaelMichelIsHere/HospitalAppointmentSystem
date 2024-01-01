package ui;

import java.io.FileNotFoundException;

// Represents the main class for the console-based appointment system
public class Main {
    public static void main(String[] args) {
        try {
            new AppointmentSystemApp();
        } catch (FileNotFoundException e) {
            System.out.println("Oops, cannot run application: file not found");
        }
    }
}
