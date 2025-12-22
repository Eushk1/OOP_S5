package eind.trmsmo.dto;

import java.time.LocalDate;

public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String specialization;

    public DoctorDTO() {}

    public DoctorDTO(Long id, String firstName, String lastName, LocalDate birthDate, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.specialization = specialization;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}