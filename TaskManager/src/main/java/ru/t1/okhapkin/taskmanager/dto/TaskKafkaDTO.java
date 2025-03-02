package ru.t1.okhapkin.taskmanager.dto;

import java.util.UUID;

public record TaskKafkaDTO(
        UUID id,
        String changes
) {}
