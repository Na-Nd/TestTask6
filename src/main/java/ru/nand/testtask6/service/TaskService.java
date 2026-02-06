package ru.nand.testtask6.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nand.testtask6.dto.TaskCreateRequest;
import ru.nand.testtask6.dto.TaskResponse;
import ru.nand.testtask6.dto.TaskUpdateRequest;
import ru.nand.testtask6.entity.enums.Status;

import java.time.Instant;

public interface TaskService {

    TaskResponse create(TaskCreateRequest request);

    TaskResponse getById(Long id);

    Page<TaskResponse> getAll(Pageable pageable);

    TaskResponse update(Long id, TaskUpdateRequest request);

    void delete(Long id);

    Page<TaskResponse> getByStatus(Status status, Pageable pageable);

    Page<TaskResponse> getByCreatedRange(Instant from, Instant to, Pageable pageable);
}
