package ru.t1.okhapkin.taskmanager.dto;

public record TaskDTO (
        String title,
        String description,
        Long userID
) {}
