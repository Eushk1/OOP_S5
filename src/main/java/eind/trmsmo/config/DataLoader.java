package eind.trmsmo.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import eind.trmsmo.entity.Doctor;
import eind.trmsmo.entity.Patient;
import eind.trmsmo.entity.ServiceEntity;
import eind.trmsmo.repository.AppointmentRepository;
import eind.trmsmo.repository.DoctorRepository;
import eind.trmsmo.repository.DocumentRepository;
import eind.trmsmo.repository.PatientRepository;
import eind.trmsmo.repository.ServiceEntityRepository;
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

    @Override
    public void run(String... args) {
    
        if (!serviceEntityRepository.existsByName("Консультация")) {
            ServiceEntity consult = new ServiceEntity("Консультация");
            consult.setDescription("Консультация врача общей практики");
            serviceEntityRepository.save(consult);
        }
    
        if (!serviceEntityRepository.existsByName("Чек-ап")) {
            ServiceEntity checkup = new ServiceEntity("Чек-ап");
            checkup.setDescription("Полный медицинский осмотр");
            serviceEntityRepository.save(checkup);
        }
    
        if (doctorRepository.count() == 0) {
            Doctor doctor1 = new Doctor("Иван", "Иванов");
            doctor1.setBirthDate(LocalDate.of(1980, 3, 15));
            doctor1.setSpecialization("Терапевт");
            doctorRepository.save(doctor1);
    
            Doctor doctor2 = new Doctor("Мария", "Петрова");
            doctor2.setBirthDate(LocalDate.of(1985, 7, 20));
            doctor2.setSpecialization("Хирург");
            doctorRepository.save(doctor2);
        }
    
        if (patientRepository.count() == 0) {
            Patient p1 = new Patient("peter@example.com", "password1", "Пётр", "Сидоров");
            Patient p2 = new Patient("anna@example.com", "password2", "Анна", "Кузнецова");
            patientRepository.save(p1);
            patientRepository.save(p2);
        }
    
        System.out.println("Тестовые данные загружены (без дублей).");
    }
    
   /*  @Override
    public void run(String... args) throws Exception {
        // Сервисы/услуги
        ServiceEntity consult = new ServiceEntity("Консультация");
        consult.setDescription("Консультация врача общей практики");
        ServiceEntity checkup = new ServiceEntity("Чек-ап");
        checkup.setDescription("Полный медицинский осмотр");
        serviceEntityRepository.save(consult);
        serviceEntityRepository.save(checkup);

        // Врачи
        Doctor doctor1 = new Doctor("Иван", "Иванов");
        doctor1.setBirthDate(LocalDate.of(1980, 3, 15));
        doctor1.setSpecialization("Терапевт");
        doctorRepository.save(doctor1);

        Doctor doctor2 = new Doctor("Мария", "Петрова");
        doctor2.setBirthDate(LocalDate.of(1985, 7, 20));
        doctor2.setSpecialization("Хирург");
        doctorRepository.save(doctor2);

        // Пациенты
        Patient p1 = new Patient("peter@example.com", "password1", "Пётр", "Сидоров");
        Patient p2 = new Patient("anna@example.com", "password2", "Анна", "Кузнецова");
        patientRepository.save(p1);
        patientRepository.save(p2);

        // Записи (appointments)
        Appointment a1 = new Appointment(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), 30, "Общая консультация");
        a1.setDoctor(doctor1);
        a1.setPatient(p1);
        a1.setStatus("SCHEDULED");
        appointmentRepository.save(a1);

        Appointment a2 = new Appointment(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0), 60, "Плановая операция (консультация)");
        a2.setDoctor(doctor2);
        a2.setPatient(p2);
        a2.setStatus("SCHEDULED");
        appointmentRepository.save(a2);

        // Visits (история визитов) — привязаны к appointment
        Visit v1 = new Visit(a1, p1, LocalDateTime.now().plusDays(1).withHour(10).withMinute(30));
        v1.setNotes("Первичный осмотр. Рекомендовано обследование.");
        visitRepository.save(v1);

        // Documents (метаданные)
        Document d1 = new Document(p1, "consent_peter.pdf", "/files/consent_peter.pdf", "consent");
        documentRepository.save(d1);

        System.out.println("Тестовые данные для медклиники загружены.");
    }*/
}