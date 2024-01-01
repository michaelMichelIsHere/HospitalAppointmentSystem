package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Represents Test for AppointmentSystem

class AppointmentSystemTest {
    private AppointmentSystem appointmentSystem;

    @BeforeEach
    void runBefore() {
        appointmentSystem = new AppointmentSystem("UBC Student Hospital");
    }

    @Test
    void testConstructor() {
        assertEquals("UBC Student Hospital", appointmentSystem.getHospitalName());
        assertEquals(0, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(0, appointmentSystem.getWaitlistPatientNum());
        assertTrue(appointmentSystem.isAppointmentListEmpty());
        assertTrue(appointmentSystem.isWaitlistEmpty());
    }

    @Test
    void testBookAppointmentOnce() {
        appointmentSystem.bookAppointment(11111111);
        assertEquals(1, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(0, appointmentSystem.getWaitlistPatientNum());
        assertEquals(8,
                appointmentSystem.getAppointmentTime(11111111));
        assertEquals(-1, appointmentSystem.getAppointmentTime(22222222));
        assertEquals(-1,
                appointmentSystem.getWaitlistPosition(11111111));
    }

    @Test
    void testBookAppointmentMultipleTimes() {
        appointmentSystem.bookAppointment(11111111);
        appointmentSystem.bookAppointment(22222222);
        appointmentSystem.bookAppointment(33333333);
        List<Patient> appointmentList = appointmentSystem.getAppointmentList();
        assertEquals(3, appointmentList.size());
        assertEquals(3, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(0, appointmentSystem.getWaitlistPatientNum());
        assertEquals(10,
                appointmentSystem.getAppointmentTime(33333333));
    }

    @Test
    void testBookAppointmentUntilFull() {
        appointmentSystem.bookAppointment(11111111);
        appointmentSystem.bookAppointment(22222222);
        appointmentSystem.bookAppointment(33333333);
        appointmentSystem.bookAppointment(44444444);
        appointmentSystem.bookAppointment(55555555);
        appointmentSystem.bookAppointment(66666666);
        appointmentSystem.bookAppointment(77777777);
        appointmentSystem.bookAppointment(88888888);
        appointmentSystem.bookAppointment(99999999);


        assertEquals(9, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(0, appointmentSystem.getWaitlistPatientNum());
    }

    @Test
    void testBookAppointmentSystemUntilWaitlist() {
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
        appointmentSystem.bookAppointment(98765432);

        List<Patient> appointmentList = appointmentSystem.getAppointmentList();
        List<Patient> waitList = appointmentSystem.getWaitlist();
        assertEquals(9, appointmentList.size());
        assertEquals(2, waitList.size());
        assertEquals(9, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(2, appointmentSystem.getWaitlistPatientNum());
        assertEquals(1,
                appointmentSystem.getWaitlistPosition(12345678));
        assertEquals(2,
                appointmentSystem.getWaitlistPosition(98765432));
    }

    @Test
    void testCancelAppointmentOnce() {
        appointmentSystem.bookAppointment(11111111);
        appointmentSystem.cancelAppointment(11111111);
        assertTrue(appointmentSystem.isAppointmentListEmpty());
    }

    @Test
    void testCancelAppointmentWithoutReplace() {
        appointmentSystem.bookAppointment(11111111);
        appointmentSystem.bookAppointment(22222222);
        appointmentSystem.bookAppointment(33333333);
        appointmentSystem.bookAppointment(44444444);
        appointmentSystem.bookAppointment(55555555);
        appointmentSystem.bookAppointment(66666666);
        appointmentSystem.bookAppointment(77777777);
        appointmentSystem.bookAppointment(88888888);
        appointmentSystem.bookAppointment(99999999);

        appointmentSystem.cancelAppointment(88888888);

        assertFalse(appointmentSystem.isAppointmentListEmpty());
        assertEquals(9, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(0, appointmentSystem.getWaitlistPatientNum());
        assertEquals(16, appointmentSystem.getAppointmentTime(99999999));
    }

    @Test
    void testCancelAppointmentWithReplace() {
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
        appointmentSystem.bookAppointment(98765432);


        appointmentSystem.cancelAppointment(44444444);

        assertEquals(9, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(1, appointmentSystem.getWaitlistPatientNum());
        assertEquals(11, appointmentSystem.getAppointmentTime(12345678));
        assertEquals(1, appointmentSystem.getWaitlistPosition(98765432));
    }

    @Test
    void testCancelAppointmentNotFound() {
        appointmentSystem.bookAppointment(11111111);
        appointmentSystem.bookAppointment(22222222);
        appointmentSystem.bookAppointment(33333333);
        appointmentSystem.bookAppointment(44444444);
        appointmentSystem.bookAppointment(55555555);
        appointmentSystem.bookAppointment(66666666);
        appointmentSystem.bookAppointment(77777777);
        appointmentSystem.bookAppointment(88888888);
        appointmentSystem.bookAppointment(99999999);

        assertFalse(appointmentSystem.cancelAppointment(12345678));
    }

    @Test
    void testBookAndCancel() {
        appointmentSystem.bookAppointment(11111111);
        appointmentSystem.bookAppointment(22222222);
        appointmentSystem.bookAppointment(33333333);
        appointmentSystem.bookAppointment(44444444);
        appointmentSystem.bookAppointment(55555555);
        appointmentSystem.bookAppointment(66666666);
        appointmentSystem.bookAppointment(77777777);
        appointmentSystem.bookAppointment(88888888);
        appointmentSystem.bookAppointment(99999999);

        appointmentSystem.cancelAppointment(22222222);
        appointmentSystem.bookAppointment(12345678);
        assertEquals(9, appointmentSystem.getAppointmentTime(12345678));
    }

    @Test
    void testCancelAppointmentNullPatient() {
        appointmentSystem.bookAppointment(11111111);
        appointmentSystem.cancelAppointment(11111111);
        assertFalse(appointmentSystem.cancelAppointment(11111111));
    }

    @Test
    void testCancelWaitlist() {
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
        appointmentSystem.bookAppointment(98765432);

        appointmentSystem.cancelWaitlist(12345678);

        assertEquals(9, appointmentSystem.getAppointmentListPatientNum());
        assertEquals(1, appointmentSystem.getWaitlistPatientNum());
        assertEquals(1, appointmentSystem.getWaitlistPosition(98765432));
    }

    @Test
    void testCancelWaitlistNotFound() {
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
        appointmentSystem.bookAppointment(98765432);

        assertFalse(appointmentSystem.cancelWaitlist(56565656));
    }
}
