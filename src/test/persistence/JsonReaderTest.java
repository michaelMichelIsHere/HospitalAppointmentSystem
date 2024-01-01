package persistence;

import model.AppointmentSystem;
import model.Patient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Represents JsonReaderTest
// code influenced by JsonSerializationDemo https://github.com/stleary/JSON-java

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            AppointmentSystem appointmentSystem = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyAppointmentSystem() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAppointmentSystem.json");
        try {
            AppointmentSystem appointmentSystem = reader.read();
            assertEquals("UBC Vancouver Student Hospital", appointmentSystem.getHospitalName());
            assertEquals(0, appointmentSystem.getAppointmentListPatientNum());
            assertEquals(0, appointmentSystem.getWaitlistPatientNum());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralAppointmentSystem() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAppointmentSystem.json");
        try {
           AppointmentSystem appointmentSystem = reader.read();
           assertEquals("UBC Vancouver Student Hospital", appointmentSystem.getHospitalName());
           List<Patient> appointmentList = appointmentSystem.getAppointmentList();
           List<Patient> waitList = appointmentSystem.getWaitlist();
           assertEquals(9, appointmentList.size());
           assertEquals(1, waitList.size());
           checkPatient(11111111, appointmentList.get(0));
           checkPatient(22222222, appointmentList.get(1));
           checkPatient(99999999, appointmentList.get(8));
           checkPatient(12345678, waitList.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
