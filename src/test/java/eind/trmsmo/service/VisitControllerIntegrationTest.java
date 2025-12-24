package eind.trmsmo.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import eind.trmsmo.dto.CreateVisitDTO;
import eind.trmsmo.entity.Appointment;
import eind.trmsmo.entity.Doctor;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.entity.Visit;
import eind.trmsmo.repository.AppointmentRepository;
import eind.trmsmo.repository.DoctorRepository;
import eind.trmsmo.repository.PatientRepository;
import eind.trmsmo.repository.VisitRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VisitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();
        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();

        doctor = new Doctor("Иван", "Иванов");
        doctor = doctorRepository.save(doctor);

        patient = new Patient("peter@example.com", "password", "Пётр", "Сидоров");
        patient = patientRepository.save(patient);

        appointment = new Appointment(LocalDateTime.now().plusDays(1).withSecond(0).withNano(0), 30, "Консультация");
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment = appointmentRepository.save(appointment);
    }

    @Test
    void createVisitAndGetByPatient_ShouldReturnVisit() throws Exception {
        CreateVisitDTO dto = new CreateVisitDTO();
        dto.setAppointmentId(appointment.getId());
        dto.setPatientId(patient.getId());
        dto.setVisitDate(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0).withSecond(0).withNano(0));
        dto.setNotes("Первичный осмотр");

        String json = objectMapper.writeValueAsString(dto);

        String resp = mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.notes").value("Первичный осмотр"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Visit created = objectMapper.readValue(resp, Visit.class);

        mockMvc.perform(get("/api/visits/by-patient/{patientId}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
}