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

import eind.trmsmo.dto.CreatePatientDTO;
import eind.trmsmo.dto.PatientDTO;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.service.PatientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAll() {
        List<Patient> patients = patientService.findAll();
        List<PatientDTO> dtos = patients.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getById(@PathVariable Long id) {
        Optional<Patient> p = patientService.findById(id);
        return p.map(patient -> ResponseEntity.ok(toDto(patient)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientDTO> create(@Valid @RequestBody CreatePatientDTO dto) {
        Patient p = new Patient();
        p.setEmail(dto.getEmail());
        p.setPassword(dto.getPassword());
        p.setFirstName(dto.getFirstName());
        p.setLastName(dto.getLastName());
        Patient saved = patientService.save(p);
        return ResponseEntity.created(URI.create("/api/patients/" + saved.getId())).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> update(@PathVariable Long id, @Valid @RequestBody CreatePatientDTO dto) {
        Optional<Patient> existing = patientService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        Patient p = existing.get();
        p.setEmail(dto.getEmail());
        p.setPassword(dto.getPassword());
        p.setFirstName(dto.getFirstName());
        p.setLastName(dto.getLastName());
        Patient updated = patientService.save(p);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Patient> existing = patientService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private PatientDTO toDto(Patient p) {
        PatientDTO dto = new PatientDTO();
        dto.setId(p.getId());
        dto.setEmail(p.getEmail());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}