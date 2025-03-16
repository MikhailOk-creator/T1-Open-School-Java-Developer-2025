package ru.t1.okhapkin.taskmanager.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "task_t")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Task() {
    }

    public Task(UUID id, String title, String description, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public Task(String title, String description, Long userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUser() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }
}
