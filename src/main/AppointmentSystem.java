package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents an appointment system having a wait list,
// and an appointment list which can only store 9 patients,
// as each appointment time is 1 hour,
// and doctors work from 8:00 to 17:00 every day,
// Saving and Loading code influenced by JsonSerializationDemo https://github.com/stleary/JSON-java

public class AppointmentSystem implements Writable {
    private static final int TOTAL_SIZE = 9;
    private String hospitalName;
    private Patient newPatient;
    private List<Patient> appointmentList;
    private List<Patient> waitlist;

    /*
     * EFFECTS: creating a list of patients
     *          with size 9 to represent the appointment system,
     *          and creating a wait list
     */
    public AppointmentSystem(String hospitalName) {
        this.hospitalName = hospitalName;
        this.appointmentList = new ArrayList<>(TOTAL_SIZE);
        this.waitlist = new ArrayList<>();
    }

    /*
     * MODIFIES: this
     * EFFECTS: the patient will be booked into the appointment system and returns false,
     *          provided the system is not full;
     *          otherwise the patient will be booked into the wait list, and returns true.
     */
    public Boolean bookAppointment(int studentNum) {
        newPatient = new Patient(studentNum);
        if (!isAppointmentListFull()) {
            for (Patient patient: appointmentList) {
                int index = appointmentList.indexOf(patient);
                if (patient == (null)) {
                    appointmentList.set(index, newPatient);
                    EventLog.getInstance().logEvent(new Event("New Patient with student number: "
                            + studentNum + " is added to the appointment list."));
                    return false;
                }
            }
            this.appointmentList.add(newPatient);
            EventLog.getInstance().logEvent(new Event("New Patient with student number: "
                    + studentNum + " is added to the appointment list."));
            return false;
        } else {
            this.waitlist.add(newPatient);
            EventLog.getInstance().logEvent(new Event("New Patient with student number: "
                    + studentNum + " is added to the wait list."));
            return true;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: replace the patient from the appointment system with
     *          the first patient in the wait list system, and returns true,
     *          provided that there are patients in the wait list;
     *          otherwise only remove the patient from the appointment system and returns true,
     *          returns false if patient cannot be found in the appointment list
     */
    public boolean cancelAppointment(int studentNum) {
        for (Patient patient: this.appointmentList) {
            if (patient != null && patient.getStudentNum() == studentNum) {
                int index = this.appointmentList.indexOf(patient);
                if (!this.waitlist.isEmpty()) {
                    Patient firstWaitlistPatient = this.waitlist.get(0);
                    this.waitlist.remove(firstWaitlistPatient);
                    this.appointmentList.set(index, firstWaitlistPatient);
                    EventLog.getInstance().logEvent(new Event("Patient: " + studentNum
                            + " cancelled appointment, "
                            + "and patient: " + firstWaitlistPatient.getStudentNum()
                            + " was added to the appointment list"));
                } else {
                    this.appointmentList.set(index, null);
                    EventLog.getInstance().logEvent(new Event("Patient: " + studentNum
                            + " cancelled appointment"));
                }
                return true;
            }
        }
        return false;
    }

    /*
     * MODIFIES: this
     * EFFECTS: returns true if removes the patient from the wait list;
     *          returns false if patient cannot be found in the wait list
     */
    public boolean cancelWaitlist(int studentNum) {
        for (Patient patient: this.waitlist) {
            if (patient.getStudentNum() == studentNum) {
                this.waitlist.remove(patient);
                EventLog.getInstance().logEvent(new Event("Patient: " + studentNum
                        + " is removed from wait list"));
                return true;
            }
        }
        return false;
    }

    /*
     * EFFECTS: returns the appointment time of a patient,
     *          if the patient is found in the appointment system;
     *          returns -1 if the patient cannot be found
     *
     */
    public int getAppointmentTime(int studentNumber) {
        int time = 8;
        for (Patient patient: this.appointmentList) {
            if (patient != (null) && patient.getStudentNum() == studentNumber) {
                return time;
            }
            time++;
        }
        return -1;
    }

    /*
     * EFFECTS: returns the appointment list
     */
    public List<Patient> getAppointmentList() {
        return this.appointmentList;
    }

    /*
     * EFFECTS: returns the wait list position of a patient,
     *          if the patient is found in the appointment system
     *          returns -1 if the patient cannot be found
     */
    public int getWaitlistPosition(int studentNumber) {
        int pos = 1;
        for (Patient patient: this.waitlist) {
            if (patient.getStudentNum() == studentNumber) {
                return pos;
            }
            pos++;
        }

        return -1;
    }

    /*
     * EFFECTS: returns the wait list
     */
    public List<Patient> getWaitlist() {
        return this.waitlist;
    }

    /*
     * EFFECTS: returns the patient number of the appointment system (including null patient)
     */
    public int getAppointmentListPatientNum() {
        return this.appointmentList.size();
    }

    public int getWaitlistPatientNum() {
        return this.waitlist.size();
    }

    /*
     * EFFECTS: return true if the appointment system is full,
     *          otherwise return false
     */
    public boolean isAppointmentListFull() {
        if (appointmentList.contains(null)) {
            return false;
        } else {
            return (this.appointmentList.size() == TOTAL_SIZE);
        }
    }

    /*
     * EFFECTS: return true if the appointment system is empty,
     *          otherwise return false
     */
    public boolean isAppointmentListEmpty() {
        if (this.appointmentList.contains(null)) {
            return (this.appointmentList.size() - 1 == 0);
        } else {
            return (this.appointmentList.isEmpty());
        }
    }

    /*
     * EFFECTS: return true if the waitlist is empty,
     *          otherwise return false
     */
    public boolean isWaitlistEmpty() {
        return (this.waitlist.isEmpty());
    }

    /*
     * EFFECTS: returns the hospital name
     */
    public String getHospitalName() {
        return this.hospitalName;
    }

    /*
     * EFFECTS: returns the JSONObject
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", hospitalName);
        json.put("appointment list", appointmentListToJson());
        json.put("wait list", waitlistToJson());
        return json;
    }

    /*
     * EFFECTS: returns patients in the appointment system as a JSON array
     */
    private JSONArray appointmentListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Patient patient : appointmentList) {
            if (patient != null) {
                jsonArray.put(patient.toJson());
            } else {
                jsonArray.put(JSONObject.NULL);
            }
        }

        return jsonArray;
    }

    /*
     * EFFECTS: returns patients in the wait list as a JSON array
     */
    private JSONArray waitlistToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Patient patient : waitlist) {
            jsonArray.put(patient.toJson());
        }

        return jsonArray;
    }

    /*
     * MODIFIES: this
     * EFFECTS: When reading file, adds patient to the appointment list
     */
    public void addToAppointmentList(Patient patient) {
        this.appointmentList.add(patient);
    }

    /*
     * MODIFIES: this
     * EFFECTS: When reading file, adds patient to the wait list
     */
    public void addToWaitlist(Patient patient) {
        this.waitlist.add(patient);
    }
}
