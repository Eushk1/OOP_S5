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

import eind.trmsmo.dto.CreateDoctorDTO;
import eind.trmsmo.dto.DoctorDTO;
import eind.trmsmo.entity.Doctor;
import eind.trmsmo.service.DoctorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAll() {
        List<Doctor> list = doctorService.findAll();
        List<DoctorDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getById(@PathVariable Long id) {
        Optional<Doctor> d = doctorService.findById(id);
        return d.map(doctor -> ResponseEntity.ok(toDto(doctor))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> create(@Valid @RequestBody CreateDoctorDTO dto) {
        Doctor d = new Doctor();
        d.setFirstName(dto.getFirstName());
        d.setLastName(dto.getLastName());
        d.setBirthDate(dto.getBirthDate());
        d.setSpecialization(dto.getSpecialization());
        Doctor saved = doctorService.save(d);
        return ResponseEntity.created(URI.create("/api/doctors/" + saved.getId())).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> update(@PathVariable Long id, @Valid @RequestBody CreateDoctorDTO dto) {
        Optional<Doctor> existing = doctorService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        Doctor d = existing.get();
        d.setFirstName(dto.getFirstName());
        d.setLastName(dto.getLastName());
        d.setBirthDate(dto.getBirthDate());
        d.setSpecialization(dto.getSpecialization());
        Doctor updated = doctorService.save(d);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Doctor> existing = doctorService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        doctorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private DoctorDTO toDto(Doctor d) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(d.getId());
        dto.setFirstName(d.getFirstName());
        dto.setLastName(d.getLastName());
        dto.setBirthDate(d.getBirthDate());
        dto.setSpecialization(d.getSpecialization());
        return dto;
    }
}