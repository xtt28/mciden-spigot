package io.github.xtt28.identityservice.spigot.database.dto;

import java.sql.Date;
import java.util.UUID;

import javax.annotation.Nonnull;

public record RegistrationDTO(
        int studentId,
        @Nonnull UUID playerUuid,
        @Nonnull Date createdAt) {
}
