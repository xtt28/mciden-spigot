package io.github.xtt28.identityservice.spigot.database.dto;

import java.sql.Time;
import java.util.UUID;

import javax.annotation.Nonnull;

public record VerifyIntentDTO(
        @Nonnull UUID id,
        int studentId,
        @Nonnull UUID playerUuid,
        @Nonnull Time createdAt,
        @Nonnull Time expiresAt) {
}
