package io.github.xtt28.identityservice.spigot.database.dto;

import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Nonnull;

public record RegistrationDTO(
        int studentId,
        @Nonnull UUID playerUuid,
        @Nonnull Timestamp createdAt) {
}
