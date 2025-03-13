package ru.t1.okhapkin.taskmanager.dto;

import java.util.UUID;

public record TaskResponseDTO (
        UUID id,
        String title,
        String description,
        Long userID
) {}