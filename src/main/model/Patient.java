package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a patient having a student number
// "toJson" method influenced by JsonSerializationDemo https://github.com/stleary/JSON-java
public class Patient implements Writable {
    private int studentNum;

    // REQUIRES: studentNum is 8 digit
    // EFFECTS: set studentNum as the student number
    //          of the patient
    public Patient(int studentNum) {
        this.studentNum = studentNum;
    }

    public int getStudentNum() {
        return this.studentNum;
    }

    // EFFECTS: returns the JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Student number", studentNum);
        return json;
    }
}
