package ru.nand.testtask6.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCreateRequest {
    @NotBlank(message = "Название задачи не может быть пустым")
    private String title;

    private String description;

    @NotBlank(message = "Задачу необходимо кому-то назначить")
    private String assignee;
}
