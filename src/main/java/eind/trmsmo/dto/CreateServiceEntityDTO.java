package eind.trmsmo.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateServiceEntityDTO {
    @NotBlank(message = "Название услуги обязательно")
    private String name;

    private String description;

    public CreateServiceEntityDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}