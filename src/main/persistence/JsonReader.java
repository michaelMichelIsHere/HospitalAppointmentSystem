package persistence;

import model.AppointmentSystem;
import model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads appointment system from JSON data stored in file
// code influenced by JsonSerializationDemo https://github.com/stleary/JSON-java

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads AppointmentSystem from file and returns it;
    // throws IOException if an error occurs reading data from file
    public AppointmentSystem read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAppointmentSystem(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses appointment system from JSON object and returns it
    private AppointmentSystem parseAppointmentSystem(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        AppointmentSystem appointmentSystem = new AppointmentSystem(name);
        addPatientsToAppointmentList(appointmentSystem, jsonObject);
        addPatientsToWaitList(appointmentSystem, jsonObject);
        return appointmentSystem;
    }

    // MODIFIES: appointmentSystem
    // EFFECTS: parses appointment list from JSON object and adds them to appointment list
    private void addPatientsToAppointmentList(AppointmentSystem appointmentSystem, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("appointment list");
        for (Object json : jsonArray) {
            if (json == JSONObject.NULL) {
                appointmentSystem.addToAppointmentList(null);
            } else {
                JSONObject patient = (JSONObject) json;
                int studentNum = patient.getInt("Student number");
                Patient p = new Patient(studentNum);
                appointmentSystem.addToAppointmentList(p);
            }
        }
    }

    // MODIFIES: appointmentSystem
    // EFFECTS: parses wait list from JSON object and adds them to wait list
    private void addPatientsToWaitList(AppointmentSystem appointmentSystem, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("wait list");
        for (Object json : jsonArray) {
            JSONObject patient = (JSONObject) json;
            int studentNum = patient.getInt("Student number");
            Patient p = new Patient(studentNum);
            appointmentSystem.addToWaitlist(p);
        }
    }
}
