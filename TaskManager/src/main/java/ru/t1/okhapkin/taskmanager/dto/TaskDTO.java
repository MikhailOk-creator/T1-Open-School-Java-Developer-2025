package ru.t1.okhapkin.taskmanager.dto;

import java.util.UUID;

public record TaskDTO (
        String title,
        String description,
        UUID userID
) {}
