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

import eind.trmsmo.dto.CreateVisitDTO;
import eind.trmsmo.dto.VisitDTO;
import eind.trmsmo.entity.Appointment;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.entity.Visit;
import eind.trmsmo.service.AppointmentService;
import eind.trmsmo.service.PatientService;
import eind.trmsmo.service.VisitService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<VisitDTO>> getAll() {
        List<Visit> list = visitService.findAll();
        List<VisitDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getById(@PathVariable Long id) {
        Optional<Visit> opt = visitService.findById(id);
        return opt.map(v -> ResponseEntity.ok(toDto(v)))
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VisitDTO> create(@Valid @RequestBody CreateVisitDTO dto) {
        Optional<Appointment> appointmentOpt = appointmentService.findById(dto.getAppointmentId());
        Optional<Patient> patientOpt = patientService.findById(dto.getPatientId());
        if (appointmentOpt.isEmpty() || patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Visit v = new Visit();
        v.setAppointment(appointmentOpt.get());
        v.setPatient(patientOpt.get());
        v.setVisitDate(dto.getVisitDate());
        v.setNotes(dto.getNotes());

        Visit saved = visitService.save(v);
        return ResponseEntity.created(URI.create("/api/visits/" + saved.getId())).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateVisitDTO dto
    ) {
        Optional<Visit> existing = visitService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Appointment> appointmentOpt = appointmentService.findById(dto.getAppointmentId());
        Optional<Patient> patientOpt = patientService.findById(dto.getPatientId());
        if (appointmentOpt.isEmpty() || patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Visit v = existing.get();
        v.setAppointment(appointmentOpt.get());
        v.setPatient(patientOpt.get());
        v.setVisitDate(dto.getVisitDate());
        v.setNotes(dto.getNotes());

        Visit updated = visitService.save(v);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Visit> existing = visitService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        visitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<VisitDTO>> findByPatient(@PathVariable Long patientId) {
        List<Visit> list = visitService.findByPatientId(patientId);
        List<VisitDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private VisitDTO toDto(Visit v) {
        VisitDTO dto = new VisitDTO();
        dto.setId(v.getId());
        if (v.getAppointment() != null) dto.setAppointmentId(v.getAppointment().getId());
        if (v.getPatient() != null) dto.setPatientId(v.getPatient().getId());
        dto.setVisitDate(v.getVisitDate());
        dto.setNotes(v.getNotes());
        return dto;
    }
}