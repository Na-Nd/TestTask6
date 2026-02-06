package ru.nand.testtask6.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.nand.testtask6.dto.TaskCreateRequest;
import ru.nand.testtask6.dto.TaskResponse;
import ru.nand.testtask6.dto.TaskUpdateRequest;
import ru.nand.testtask6.entity.Task;
import ru.nand.testtask6.entity.enums.Status;
import ru.nand.testtask6.repository.TaskRepository;
import ru.nand.testtask6.util.exception.ResourceNotFoundException;

import java.time.Instant;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse create(TaskCreateRequest request) {
        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .assignee(request.getAssignee())
                .status(Status.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return toResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse getById(Long id) {
        return toResponse(taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id=" + id + " не найдена")));
    }

    @Override
    public Page<TaskResponse> getAll(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id=" + id + " не найдена"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignee(request.getAssignee());
        task.setStatus(request.getStatus());

        return toResponse(taskRepository.save(task));
    }

    @Override
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) throw new ResourceNotFoundException("Задача с id=" + id + " не найдена");

        taskRepository.deleteById(id);
    }

    @Override
    public Page<TaskResponse> getByStatus(Status status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable).map(this::toResponse);
    }

    @Override
    public Page<TaskResponse> getByCreatedRange(Instant from, Instant to, Pageable pageable) {
        return taskRepository.findByCreatedAtBetween(from, to, pageable).map(this::toResponse);
    }

    /// Вспомогательный метод-маппер
    private TaskResponse toResponse(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .description(task.getDescription())
                .assignee(task.getAssignee())
                .build();
    }

}
