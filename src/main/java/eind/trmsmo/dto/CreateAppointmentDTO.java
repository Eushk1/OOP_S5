package eind.trmsmo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateAppointmentDTO {
    @NotNull(message = "Дата и время приёма обязательны")
    private LocalDateTime appointmentTime;

    @Positive(message = "Длительность должна быть положительной")
    private Integer durationMinutes;

    private String reason;

    @NotNull(message = "ID врача обязателен")
    private Long doctorId;

    @NotNull(message = "ID пациента обязателен")
    private Long patientId;

    public CreateAppointmentDTO() {}

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
}