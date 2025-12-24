package eind.trmsmo.service;

import java.util.List;
import java.util.Optional;

import eind.trmsmo.entity.Visit;

public interface VisitService {
    List<Visit> findAll();
    Optional<Visit> findById(Long id);
    Visit save(Visit visit);
    void deleteById(Long id);
    List<Visit> findByPatientId(Long patientId);
}