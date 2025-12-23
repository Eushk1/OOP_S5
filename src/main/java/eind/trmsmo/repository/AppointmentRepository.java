package eind.trmsmo.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eind.trmsmo.entity.Appointment;
import eind.trmsmo.entity.Doctor;
import eind.trmsmo.entity.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByAppointmentTimeBetween(LocalDateTime from, LocalDateTime to);
    Optional<Appointment> findByDoctorAndPatientAndAppointmentTime(
        Doctor doctor,
        Patient patient,
        LocalDateTime appointmentTime
    );
}
