package eind.trmsmo.service;

import java.util.List;
import java.util.Optional;

import eind.trmsmo.entity.Doctor;

public interface DoctorService {
    List<Doctor> findAll();
    Optional<Doctor> findById(Long id);
    Doctor save(Doctor doctor);
    void deleteById(Long id);
    List<Doctor> findBySpecialization(String specialization);
}