package ru.nand.testtask6.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nand.testtask6.entity.Task;
import ru.nand.testtask6.entity.enums.Status;

import java.time.Instant;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByCreatedAtBetween(Instant start, Instant end, Pageable pageable);

    long countByStatus(Status status);
}
