package persistence;

import model.Patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Represents JsonTest
// code influenced by JsonSerializationDemo https://github.com/stleary/JSON-java
public class JsonTest {
    protected void checkPatient(int studentNum, Patient patient) {
        assertEquals(studentNum, patient.getStudentNum());
    }
}
