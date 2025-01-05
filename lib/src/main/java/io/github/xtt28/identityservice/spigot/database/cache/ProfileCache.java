package io.github.xtt28.identityservice.spigot.database.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import io.github.xtt28.identityservice.spigot.database.dto.StudentDTO;

public final class ProfileCache {

    private ProfileCache() {

    }

    private static final Map<UUID, StudentDTO> profileCache = new HashMap<>();

    public static void cachePlayer(@Nonnull final UUID playerUuid, @Nonnull final StudentDTO studentDto) {
        profileCache.put(playerUuid, studentDto);
    }

    public static StudentDTO getStudentByPlayer(@Nonnull final UUID playerUuid) {
        return profileCache.get(playerUuid);
    }
}
