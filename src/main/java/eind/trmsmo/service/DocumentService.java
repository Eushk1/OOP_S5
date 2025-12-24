package eind.trmsmo.service;

import java.util.List;
import java.util.Optional;

import eind.trmsmo.entity.Document;

public interface DocumentService {
    List<Document> findAll();
    Optional<Document> findById(Long id);
    Document save(Document document);
    void deleteById(Long id);
    List<Document> findByPatientId(Long patientId);
}