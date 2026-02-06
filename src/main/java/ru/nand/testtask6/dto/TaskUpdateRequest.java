package ru.nand.testtask6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.nand.testtask6.entity.enums.Status;

@Data
public class TaskUpdateRequest {
    @NotBlank(message = "Название задачи не может быть пустым")
    private String title;

    private String description;

    @NotBlank(message = "Задачу необходимо кому-то назначить")
    private String assignee;

    @NotNull(message = "Статус задачи не может быть пустым")
    private Status status;
}
