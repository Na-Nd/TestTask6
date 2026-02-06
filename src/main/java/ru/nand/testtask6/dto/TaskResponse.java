package ru.nand.testtask6.dto;

import lombok.Builder;
import lombok.Data;
import ru.nand.testtask6.entity.enums.Status;

import java.time.Instant;

@Data
@Builder
public class TaskResponse {
    private Long id;

    private String title;

    private Status status;

    private Instant createdAt;

    private Instant updatedAt;

    private String description;

    private String assignee;
}
