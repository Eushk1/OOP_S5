package eind.trmsmo.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import eind.trmsmo.entity.Patient;
import eind.trmsmo.repository.PatientRepository;
import eind.trmsmo.service.impl.PatientServiceImpl;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient sample;

    @BeforeEach
    void setUp() {
        sample = new Patient("peter@example.com", "password", "Пётр", "Сидоров");
        sample.setId(1L);
        sample.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnList() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(sample));
        List<Patient> list = patientService.findAll();
        assertEquals(1, list.size());
        verify(patientRepository).findAll();
    }

    @Test
    void findById_WhenExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(sample));
        Optional<Patient> res = patientService.findById(1L);
        assertTrue(res.isPresent());
        assertEquals("peter@example.com", res.get().getEmail());
        verify(patientRepository).findById(1L);
    }

    @Test
    void save_ShouldCallRepository() {
        when(patientRepository.save(sample)).thenReturn(sample);
        Patient res = patientService.save(sample);
        assertEquals("peter@example.com", res.getEmail());
        verify(patientRepository).save(sample);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(patientRepository).deleteById(1L);
        patientService.deleteById(1L);
        verify(patientRepository).deleteById(1L);
    }

    @Test
    void findByEmail_ShouldDelegate() {
        when(patientRepository.findByEmail("peter@example.com")).thenReturn(Optional.of(sample));
        Optional<Patient> res = patientService.findByEmail("peter@example.com");
        assertTrue(res.isPresent());
        verify(patientRepository).findByEmail("peter@example.com");
    }
}