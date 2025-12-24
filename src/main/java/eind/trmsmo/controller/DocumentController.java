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

import eind.trmsmo.dto.CreateDocumentDTO;
import eind.trmsmo.dto.DocumentDTO;
import eind.trmsmo.entity.Document;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.service.DocumentService;
import eind.trmsmo.service.PatientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAll() {
        List<Document> list = documentService.findAll();
        List<DocumentDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getById(@PathVariable Long id) {
        Optional<Document> d = documentService.findById(id);
        return d.map(document -> ResponseEntity.ok(toDto(document))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> create(@Valid @RequestBody CreateDocumentDTO dto) {
        Optional<Patient> pOpt = patientService.findById(dto.getPatientId());
        if (pOpt.isEmpty()) return ResponseEntity.badRequest().build();

        Document doc = new Document();
        doc.setPatient(pOpt.get());
        doc.setFilename(dto.getFilename());
        doc.setFilePath(dto.getFilePath());
        doc.setDocumentType(dto.getDocumentType());
        Document saved = documentService.save(doc);
        return ResponseEntity.created(URI.create("/api/documents/" + saved.getId())).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> update(@PathVariable Long id, @Valid @RequestBody CreateDocumentDTO dto) {
        Optional<Document> existing = documentService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        Optional<Patient> pOpt = patientService.findById(dto.getPatientId());
        if (pOpt.isEmpty()) return ResponseEntity.badRequest().build();

        Document doc = existing.get();
        doc.setPatient(pOpt.get());
        doc.setFilename(dto.getFilename());
        doc.setFilePath(dto.getFilePath());
        doc.setDocumentType(dto.getDocumentType());
        Document updated = documentService.save(doc);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Document> existing = documentService.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<DocumentDTO>> findByPatient(@PathVariable Long patientId) {
        List<Document> list = documentService.findByPatientId(patientId);
        List<DocumentDTO> dtos = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private DocumentDTO toDto(Document d) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(d.getId());
        if (d.getPatient() != null) dto.setPatientId(d.getPatient().getId());
        dto.setFilename(d.getFilename());
        dto.setFilePath(d.getFilePath());
        dto.setUploadedAt(d.getUploadedAt());
        dto.setDocumentType(d.getDocumentType());
        return dto;
    }
}