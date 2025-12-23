package eind.trmsmo.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import eind.trmsmo.entity.Appointment;
import eind.trmsmo.entity.Doctor;
import eind.trmsmo.entity.Document;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.entity.ServiceEntity;
import eind.trmsmo.entity.User;
import eind.trmsmo.entity.Visit;
import eind.trmsmo.repository.AppointmentRepository;
import eind.trmsmo.repository.DoctorRepository;
import eind.trmsmo.repository.DocumentRepository;
import eind.trmsmo.repository.PatientRepository;
import eind.trmsmo.repository.ServiceEntityRepository;
import eind.trmsmo.repository.UserRepository;
import eind.trmsmo.repository.VisitRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ServiceEntityRepository serviceEntityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        /*
         * =========================
         * USERS
         * =========================
         */

        User adminUser = userRepository.findByEmail("admin@medclinic.com")
            .orElseGet(() -> {
                User u = new User();
                u.setEmail("admin@medclinic.com");
                u.setPassword(passwordEncoder.encode("password123"));
                u.setFirstName("Администратор");
                u.setLastName("Системы");
                u.setRole("ADMIN");
                u.setCreatedAt(LocalDateTime.now());
                return userRepository.save(u);
            });

        User patientUser = userRepository.findByEmail("user@medclinic.com")
            .orElseGet(() -> {
                User u = new User();
                u.setEmail("user@medclinic.com");
                u.setPassword(passwordEncoder.encode("password123"));
                u.setFirstName("Пользователь");
                u.setLastName("Иванов");
                u.setRole("USER");
                u.setCreatedAt(LocalDateTime.now());
                return userRepository.save(u);
            });

        /*
         * =========================
         * SERVICES
         * =========================
         */

        ServiceEntity consultation = serviceEntityRepository.findByName("Консультация")
            .orElseGet(() -> {
                ServiceEntity s = new ServiceEntity("Консультация");
                s.setDescription("Консультация врача общей практики");
                return serviceEntityRepository.save(s);
            });

        ServiceEntity checkup = serviceEntityRepository.findByName("Чек-ап")
            .orElseGet(() -> {
                ServiceEntity s = new ServiceEntity("Чек-ап");
                s.setDescription("Полный медицинский осмотр");
                return serviceEntityRepository.save(s);
            });

        /*
         * =========================
         * DOCTORS
         * =========================
         */

        Doctor doctor1 = doctorRepository
            .findByFirstNameAndLastName("Иван", "Иванов")
            .orElseGet(() -> {
                Doctor d = new Doctor("Иван", "Иванов");
                d.setBirthDate(LocalDate.of(1980, 3, 15));
                d.setSpecialization("Терапевт");
                return doctorRepository.save(d);
            });

        Doctor doctor2 = doctorRepository
            .findByFirstNameAndLastName("Мария", "Петрова")
            .orElseGet(() -> {
                Doctor d = new Doctor("Мария", "Петрова");
                d.setBirthDate(LocalDate.of(1985, 7, 20));
                d.setSpecialization("Хирург");
                return doctorRepository.save(d);
            });

        /*
         * =========================
         * PATIENTS
         * =========================
         */

        Patient patient1 = patientRepository.findByEmail("peter@example.com")
            .orElseGet(() -> {
                Patient p = new Patient(
                    "peter@example.com",
                    passwordEncoder.encode("password1"),
                    "Пётр",
                    "Сидоров"
                );
                p.setUser(patientUser);
                return patientRepository.save(p);
            });

        /*
         * =========================
         * APPOINTMENTS
         * =========================
         */

        LocalDateTime appointmentTime =
            LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0);

        Appointment appointment1 = appointmentRepository
            .findByDoctorAndPatientAndAppointmentTime(
                doctor1,
                patient1,
                appointmentTime
            )
            .orElseGet(() -> {
                Appointment a = new Appointment(
                    appointmentTime,
                    30,
                    "Общая консультация"
                );
                a.setDoctor(doctor1);
                a.setPatient(patient1);
                a.setStatus("SCHEDULED");
                return appointmentRepository.save(a);
            });

        /*
         * =========================
         * VISITS
         * =========================
         */

        if (!visitRepository.existsByAppointment(appointment1)) {
            Visit visit = new Visit(
                appointment1,
                patient1,
                appointmentTime.plusMinutes(30)
            );
            visit.setNotes("Первичный осмотр. Рекомендовано обследование.");
            visitRepository.save(visit);
        }

        /*
         * =========================
         * DOCUMENTS
         * =========================
         */

        if (!documentRepository.existsByFilename("consent_peter.pdf")) {
            Document doc = new Document(
                patient1,
                "consent_peter.pdf",
                "/files/consent_peter.pdf",
                "consent"
            );
            documentRepository.save(doc);
        }

        System.out.println("✔ Тестовые данные успешно загружены");
    }
}
