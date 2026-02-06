package ru.nand.testtask6.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.nand.testtask6.entity.enums.Status;

import java.time.Instant;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private Instant createdAt;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private Instant updatedAt;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String assignee; // имя пользователя, которому назначена задача
}
