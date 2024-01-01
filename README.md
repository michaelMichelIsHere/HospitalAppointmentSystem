# HospitalAppointmentSystem


## Background

At UBC, students can use online appointment system to book an appointment with a family doctor.
However, there is an inconvenience: Sometimes the website shows *"No available time to book"*.

Therefore, I want to design an online booking system with a waitlist option to help both students and staff.

## Functionality

- let a patient book an appointment at a hospital (*Or get into waitlist if there isn't available time*)
- let a patient cancel an upcoming appointment
- let administrative staff to manage the current appointment system

## Things to notice about waitlist
- If someone cancelled their appointment, the first person in the waitlist will automatically get an appointment
- There is unlimited places on the waitlist
- The position in a waitlist is viewable

## Intended User Group

- Patients
- Administrative staff at hospital

## User Stories

- As a patient, I want to be able to book an appointment.
- As a patient, if the appointment system is full, I want to be able to be added to the wait list.
- As a patient, I want to be able to view my current position in the waitlist system.
- As a patient, I want to be able to cancel my upcoming appointment.
- As a patient, I want to be able to cancel my position in the waitlist system.
- As a patient, I want to be able to save the current appointment system.
- As a patient, when I start the application, I want to be able to load the saved appointment system.
- As a staff, I want to be able to add a student to the current appointment.
- As a staff, if the appointment system is full, I want to be able to add a student to the wait list.
- As a staff, I want to be able to remove a student from the appointment and waitlist system.
- As a staff, I want to be able to view a list of students in the current appointment and the waitlist system.
- As a staff, I want to be able to save the current appointment system.
- As a staff, when I start the application, I want to be able to load the saved appointment system.

## Instructions for User
- Run "AppointmentSystemGUI", and the main panel will open.
- Once you are in main panel, you will see 4 buttons (Patient, Staff, Save, Load)

A: *If you click "Patient" button:* you will be directed to the patient panel. Here, you can perform 5 actions:
- Book Appointment (i.e. adding multiple Xs to a Y)
- Cancel Appointment
- Cancel Waitlist (i.e. remove yourself from the wait list)
- View Waitlist Position
- Back to Main Panel

B: *If you click "Staff" button:* you will be directed to the staff panel. Here, you can perform 7 actions:
- Book Appointment (i.e. adding multiple Xs to a Y)
- Cancel Appointment
- Cancel Waitlist (i.e. remove a patient from the wait list)
- View Appointment Time
- View Waitlist Position
- View Whole Lists (i.e. view all the patients in the appointment list and wait list)
- Back to Main Panel

C: *If you click "Save" button:* you can save the state of the application to file

D: *If you click "Load" button:* you can load the state of the application from file

**PS**: *You can locate my visual component by going to the data folder*
