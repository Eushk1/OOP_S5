package eind.trmsmo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateDocumentDTO {
    @NotNull(message = "ID пациента обязателен")
    private Long patientId;

    @NotBlank(message = "Имя файла обязательно")
    private String filename;

    @NotBlank(message = "Путь к файлу обязателен")
    private String filePath;

    private String documentType;

    public CreateDocumentDTO() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
}