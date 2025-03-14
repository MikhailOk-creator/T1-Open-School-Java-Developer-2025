package ru.t1.okhapkin.taskmanager.dto;

public record TaskRequestDTO (
        String title,
        String description,
        Long userID
) {}
