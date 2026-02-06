package ru.nand.testtask6.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nand.testtask6.dto.TaskCreateRequest;
import ru.nand.testtask6.dto.TaskResponse;
import ru.nand.testtask6.dto.TaskUpdateRequest;
import ru.nand.testtask6.entity.enums.Status;
import ru.nand.testtask6.service.TaskService;

import java.time.Instant;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskCreateRequest request){
        return ResponseEntity.status(201).body(taskService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getCurrent(@PathVariable Long id){
        return ResponseEntity.status(200).body(taskService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateCurrent(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request){
        return ResponseEntity.status(200).body(taskService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrent(@PathVariable Long id){
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /// Получение списка всех задач с пагинацией
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        if (status != null) {
            return ResponseEntity.status(200).body(taskService.getByStatus(status, pageable));
        }
        if (from != null && to != null) {
            return ResponseEntity.status(200).body(taskService.getByCreatedRange(from, to, pageable));
        }

        return ResponseEntity.status(200).body(taskService.getAll(pageable));
    }

    /// Получение активных задач с пагинацией
    @GetMapping("/active")
    public ResponseEntity<Page<TaskResponse>> listActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.status(200).body(taskService.getByStatus(Status.ACTIVE, pageable));
    }

    /// Получение выполненных задач с пагинацией
    @GetMapping("/completed")
    public ResponseEntity<Page<TaskResponse>> listCompleted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return ResponseEntity.status(200).body(taskService.getByStatus(Status.COMPLETED, pageable));
    }
}
