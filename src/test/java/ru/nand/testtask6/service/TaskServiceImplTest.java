package ru.nand.testtask6.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import ru.nand.testtask6.dto.TaskCreateRequest;
import ru.nand.testtask6.dto.TaskResponse;
import ru.nand.testtask6.dto.TaskUpdateRequest;
import ru.nand.testtask6.entity.Task;
import ru.nand.testtask6.entity.enums.Status;
import ru.nand.testtask6.repository.TaskRepository;
import ru.nand.testtask6.util.exception.ResourceNotFoundException;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnResponse() {
        TaskCreateRequest req = new TaskCreateRequest();
        req.setTitle("Title");
        req.setDescription("Desc");
        req.setAssignee("user");

        Task saved = Task.builder()
                .id(1L)
                .title(req.getTitle())
                .description(req.getDescription())
                .assignee(req.getAssignee())
                .status(Status.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponse resp = taskService.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getStatus()).isEqualTo(Status.ACTIVE);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getById_whenExists_shouldReturn() {
        Task t = Task.builder()
                .id(2L)
                .title("t")
                .description("d")
                .assignee("a")
                .status(Status.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(taskRepository.findById(2L)).thenReturn(Optional.of(t));

        TaskResponse resp = taskService.getById(2L);

        assertThat(resp.getId()).isEqualTo(2L);
        assertThat(resp.getTitle()).isEqualTo("t");
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_shouldSaveChanges() {
        Task existing = Task.builder()
                .id(3L)
                .title("old")
                .description("old")
                .assignee("old")
                .status(Status.ACTIVE)
                .createdAt(Instant.now().minusSeconds(3600))
                .updatedAt(Instant.now().minusSeconds(3600))
                .build();

        TaskUpdateRequest req = new TaskUpdateRequest();
        req.setTitle("new");
        req.setDescription("new desc");
        req.setAssignee("newUser");
        req.setStatus(Status.COMPLETED);

        when(taskRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponse resp = taskService.update(3L, req);

        assertThat(resp.getTitle()).isEqualTo("new");
        assertThat(resp.getStatus()).isEqualTo(Status.COMPLETED);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void delete_whenNotExists_shouldThrow() {
        when(taskRepository.existsById(42L)).thenReturn(false);
        assertThatThrownBy(() -> taskService.delete(42L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAll_shouldReturnPage() {
        Task t1 = Task.builder().id(10L).title("t1").status(Status.ACTIVE).createdAt(Instant.now()).updatedAt(Instant.now()).assignee("a").build();
        Page<Task> page = new PageImpl<>(List.of(t1));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<TaskResponse> res = taskService.getAll(PageRequest.of(0, 10));

        assertThat(res.getTotalElements()).isEqualTo(1);
        assertThat(res.getContent().getFirst().getTitle()).isEqualTo("t1");
    }
}
