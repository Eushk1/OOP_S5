package eind.trmsmo.service;

import java.util.List;
import java.util.Optional;

import eind.trmsmo.entity.Patient;

public interface PatientService {
    List<Patient> findAll();
    Optional<Patient> findById(Long id);
    Patient save(Patient patient);
    void deleteById(Long id);
    Optional<Patient> findByEmail(String email);
}