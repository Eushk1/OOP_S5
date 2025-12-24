package eind.trmsmo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateVisitDTO {
    @NotNull(message = "ID записи обязателен")
    private Long appointmentId;

    @NotNull(message = "ID пациента обязателен")
    private Long patientId;

    @NotNull(message = "Дата визита обязательна")
    private LocalDateTime visitDate;

    private String notes;

    public CreateVisitDTO() {}

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public LocalDateTime getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDateTime visitDate) { this.visitDate = visitDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}