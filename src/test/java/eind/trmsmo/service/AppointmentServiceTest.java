package eind.trmsmo.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import eind.trmsmo.entity.Appointment;
import eind.trmsmo.entity.Doctor;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.repository.AppointmentRepository;
import eind.trmsmo.service.impl.AppointmentServiceImpl;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Appointment sample;

    @BeforeEach
    void setUp() {
        Doctor doc = new Doctor("Иван", "Иванов");
        doc.setId(1L);
        Patient pat = new Patient("peter@example.com", "pass", "Пётр", "Сидоров");
        pat.setId(1L);
        sample = new Appointment(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), 30, "Консультация");
        sample.setId(1L);
        sample.setDoctor(doc);
        sample.setPatient(pat);
    }

    @Test
    void findAll_ShouldReturnAll() {
        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(sample));
        List<Appointment> list = appointmentService.findAll();
        assertEquals(1, list.size());
        verify(appointmentRepository).findAll();
    }

    @Test
    void findById_WhenExists() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(sample));
        Optional<Appointment> opt = appointmentService.findById(1L);
        assertTrue(opt.isPresent());
        assertEquals("Консультация", opt.get().getReason());
        verify(appointmentRepository).findById(1L);
    }

    @Test
    void save_ShouldPersist() {
        when(appointmentRepository.save(sample)).thenReturn(sample);
        Appointment saved = appointmentService.save(sample);
        assertNotNull(saved);
        assertEquals(sample.getReason(), saved.getReason());
        verify(appointmentRepository).save(sample);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(appointmentRepository).deleteById(1L);
        appointmentService.deleteById(1L);
        verify(appointmentRepository).deleteById(1L);
    }

    @Test
    void findByDoctorId_ShouldDelegate() {
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(Arrays.asList(sample));
        List<Appointment> res = appointmentService.findByDoctorId(1L);
        assertEquals(1, res.size());
        verify(appointmentRepository).findByDoctorId(1L);
    }

    @Test
    void findByPatientId_ShouldDelegate() {
        when(appointmentRepository.findByPatientId(1L)).thenReturn(Arrays.asList(sample));
        List<Appointment> res = appointmentService.findByPatientId(1L);
        assertEquals(1, res.size());
        verify(appointmentRepository).findByPatientId(1L);
    }
}