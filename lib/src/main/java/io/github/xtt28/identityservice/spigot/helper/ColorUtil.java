package io.github.xtt28.identityservice.spigot.helper;

import javax.annotation.Nonnull;

import net.md_5.bungee.api.ChatColor;

public final class ColorUtil {
    
    private ColorUtil() {
    }

    public static @Nonnull String translate(@Nonnull final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
