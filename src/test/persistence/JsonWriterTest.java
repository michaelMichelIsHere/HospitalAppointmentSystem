package persistence;

import model.AppointmentSystem;
import model.Patient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Represents JsonWriterTest
// code influenced by JsonSerializationDemo https://github.com/stleary/JSON-java
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            AppointmentSystem appointmentSystem = new AppointmentSystem("UBC Vancouver Student Hospital");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyAppointmentSystem() {
        try {
            AppointmentSystem appointmentSystem = new AppointmentSystem("UBC Vancouver Student Hospital");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyAppointmentSystem.json");
            writer.open();
            writer.write(appointmentSystem);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAppointmentSystem.json");
            appointmentSystem = reader.read();
            assertEquals("UBC Vancouver Student Hospital", appointmentSystem.getHospitalName());
            assertEquals(0, appointmentSystem.getAppointmentListPatientNum());
            assertEquals(0, appointmentSystem.getWaitlistPatientNum());
        } catch (IOException e){
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralAppointmentSystem() {
        try {
            AppointmentSystem appointmentSystem = new AppointmentSystem("UBC Vancouver Student Hospital");

            appointmentSystem.bookAppointment(11111111);
            appointmentSystem.bookAppointment(22222222);
            appointmentSystem.bookAppointment(33333333);
            appointmentSystem.bookAppointment(44444444);
            appointmentSystem.bookAppointment(55555555);
            appointmentSystem.bookAppointment(66666666);
            appointmentSystem.bookAppointment(77777777);
            appointmentSystem.bookAppointment(88888888);
            appointmentSystem.bookAppointment(99999999);

            appointmentSystem.bookAppointment(12345678);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAppointmentSystem.json");
            writer.open();
            writer.write(appointmentSystem);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAppointmentSystem.json");
            appointmentSystem = reader.read();
            assertEquals("UBC Vancouver Student Hospital", appointmentSystem.getHospitalName());
            List<Patient> appointmentList = appointmentSystem.getAppointmentList();
            List<Patient> waitlist = appointmentSystem.getWaitlist();
            assertEquals(9, appointmentList.size());
            assertEquals(1, waitlist.size());
            checkPatient(11111111, appointmentList.get(0));
            checkPatient(22222222, appointmentList.get(1));
            checkPatient(12345678, waitlist.get(0));
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void testWriterBookAndCancelAppointmentSystem() {
        try {
            AppointmentSystem appointmentSystem = new AppointmentSystem("UBC Vancouver Student Hospital");
            appointmentSystem.bookAppointment(11111111);
            appointmentSystem.cancelAppointment(11111111);

            JsonWriter writer = new JsonWriter("./data/testWriterBookAndCancelAppointmentSystem.json");
            writer.open();
            writer.write(appointmentSystem);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterBookAndCancelAppointmentSystem.json");
            appointmentSystem = reader.read();

            List<Patient> appointmentList = appointmentSystem.getAppointmentList();
            assertEquals(1, appointmentList.size());
            assertEquals(null, appointmentList.get(0));
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

}
