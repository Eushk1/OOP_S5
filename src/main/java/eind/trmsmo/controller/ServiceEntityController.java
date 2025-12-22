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

import eind.trmsmo.dto.CreateServiceEntityDTO;
import eind.trmsmo.dto.ServiceEntityDTO;
import eind.trmsmo.entity.ServiceEntity;
import eind.trmsmo.service.ServiceEntityService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/services")
public class ServiceEntityController {

    @Autowired
    private ServiceEntityService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceEntityDTO>> getAll() {
        List<ServiceEntity> list = serviceService.findAll();
        List<ServiceEntityDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntityDTO> getById(@PathVariable Long id) {
        Optional<ServiceEntity> s = serviceService.findById(id);
        return s.map(serviceEntity -> ResponseEntity.ok(toDto(serviceEntity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceEntityDTO> create(@Valid @RequestBody CreateServiceEntityDTO dto) {
        ServiceEntity s = new ServiceEntity();
        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        ServiceEntity saved = serviceService.save(s);
        return ResponseEntity.created(URI.create("/api/services/" + saved.getId())).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntityDTO> update(@PathVariable Long id, @Valid @RequestBody CreateServiceEntityDTO dto) {
        Optional<ServiceEntity> existing = serviceService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        ServiceEntity s = existing.get();
        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        ServiceEntity updated = serviceService.save(s);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<ServiceEntity> existing = serviceService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        serviceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ServiceEntityDTO toDto(ServiceEntity s) {
        ServiceEntityDTO dto = new ServiceEntityDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        return dto;
    }
}