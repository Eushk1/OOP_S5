package eind.trmsmo.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eind.trmsmo.dto.AppointmentDTO;
import eind.trmsmo.dto.CreateAppointmentDTO;
import eind.trmsmo.entity.Appointment;
import eind.trmsmo.entity.Doctor;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.service.AppointmentService;
import eind.trmsmo.service.DoctorService;
import eind.trmsmo.service.PatientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        List<Appointment> list = appointmentService.findAll();
        List<AppointmentDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable Long id) {
        Optional<Appointment> opt = appointmentService.findById(id);
        return opt.map(a -> ResponseEntity.ok(toDto(a)))
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@Valid @RequestBody CreateAppointmentDTO dto) {
        Optional<Doctor> doctorOpt = doctorService.findById(dto.getDoctorId());
        Optional<Patient> patientOpt = patientService.findById(dto.getPatientId());
        if (doctorOpt.isEmpty() || patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Appointment a = new Appointment();
        a.setAppointmentTime(dto.getAppointmentTime());
        a.setDurationMinutes(dto.getDurationMinutes());
        a.setReason(dto.getReason());
        a.setDoctor(doctorOpt.get());
        a.setPatient(patientOpt.get());
        a.setStatus("SCHEDULED");

        Appointment saved = appointmentService.save(a);
        return ResponseEntity.created(URI.create("/api/appointments/" + saved.getId())).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateAppointmentDTO dto
    ) {
        Optional<Appointment> existing = appointmentService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Doctor> doctorOpt = doctorService.findById(dto.getDoctorId());
        Optional<Patient> patientOpt = patientService.findById(dto.getPatientId());
        if (doctorOpt.isEmpty() || patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Appointment a = existing.get();
        a.setAppointmentTime(dto.getAppointmentTime());
        a.setDurationMinutes(dto.getDurationMinutes());
        a.setReason(dto.getReason());
        a.setDoctor(doctorOpt.get());
        a.setPatient(patientOpt.get());

        Appointment updated = appointmentService.save(a);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Appointment> existing = appointmentService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        appointmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getByDoctor(@PathVariable Long doctorId) {
        List<Appointment> list = appointmentService.findByDoctorId(doctorId);
        List<AppointmentDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getByPatient(@PathVariable Long patientId) {
        List<Appointment> list = appointmentService.findByPatientId(patientId);
        List<AppointmentDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private AppointmentDTO toDto(Appointment a) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(a.getId());
        dto.setAppointmentTime(a.getAppointmentTime());
        dto.setDurationMinutes(a.getDurationMinutes());
        dto.setReason(a.getReason());
        dto.setStatus(a.getStatus());
        if (a.getDoctor() != null) {
            dto.setDoctorId(a.getDoctor().getId());
            dto.setDoctorName(a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName());
        }
        if (a.getPatient() != null) {
            dto.setPatientId(a.getPatient().getId());
            dto.setPatientName(a.getPatient().getFirstName() + " " + a.getPatient().getLastName());
        }
        return dto;
    }
}