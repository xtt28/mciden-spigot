package io.github.xtt28.identityservice.spigot.placeholder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import io.github.xtt28.identityservice.spigot.database.cache.ProfileCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public final class IdentityPlaceholderExpansion extends PlaceholderExpansion {
    
    private final PluginDescriptionFile desc;

    public IdentityPlaceholderExpansion(final PluginDescriptionFile desc) {
        this.desc = desc;
    }

    @Override
    public final @Nonnull String getAuthor() {
        return desc.getAuthors().size() > 0 ? desc.getAuthors().get(0) : "xtt28";
    }

    @Override
    public final @Nonnull String getIdentifier() {
        return "idenservice";
    }

    @Override
    public final @Nonnull String getVersion() {
        return desc.getVersion();
    }

    @Override
    public final @Nullable String onPlaceholderRequest(final Player player, final String params) {
        if (player == null)
            return null;
        
        final var profile = ProfileCache.getStudentByPlayer(player.getUniqueId());

        return switch (params) {
            case "first_name" -> profile.firstName();
            case "last_name" -> profile.lastName();
            case "full_name" -> profile.firstName() + " " + profile.lastName();
            case "academy" -> profile.academy();
            default -> null;
        };
    }
}
