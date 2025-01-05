package io.github.xtt28.identityservice.spigot.database.dto;

import javax.annotation.Nonnull;

public record StudentDTO(
        int id,
        @Nonnull String firstName,
        @Nonnull String lastName,
        @Nonnull String email,
        @Nonnull String academy) {
}
