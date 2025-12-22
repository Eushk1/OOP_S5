package eind.trmsmo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eind.trmsmo.dto.CreateAppointmentDTO;
import eind.trmsmo.entity.Appointment;
import eind.trmsmo.entity.Doctor;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.repository.AppointmentRepository;
import eind.trmsmo.repository.DoctorRepository;
import eind.trmsmo.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();

        doctor = new Doctor("Иван", "Иванов");
        doctor = doctorRepository.save(doctor);

        patient = new Patient("peter@example.com", "password", "Пётр", "Сидоров");
        patient = patientRepository.save(patient);
    }

    @Test
    void getAll_WhenEmpty_ShouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createAndGetById_ShouldReturnCreated() throws Exception {
        CreateAppointmentDTO dto = new CreateAppointmentDTO();
        dto.setAppointmentTime(LocalDateTime.now().plusDays(1).withSecond(0).withNano(0));
        dto.setDurationMinutes(30);
        dto.setReason("Консультация");
        dto.setDoctorId(doctor.getId());
        dto.setPatientId(patient.getId());

        String json = objectMapper.writeValueAsString(dto);

        String content = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reason").value("Консультация"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Получим id созданного объекта
        Appointment created = objectMapper.readValue(content, Appointment.class);

        mockMvc.perform(get("/api/appointments/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.reason").value("Консультация"));
    }

    @Test
    void getByDoctor_ShouldReturnList() throws Exception {
        Appointment a = new Appointment(LocalDateTime.now().plusDays(1).withSecond(0).withNano(0), 30, "Консультация");
        a.setDoctor(doctor);
        a.setPatient(patient);
        appointmentRepository.save(a);

        mockMvc.perform(get("/api/appointments/by-doctor/{doctorId}", doctor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}