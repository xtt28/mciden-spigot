package io.github.xtt28.identityservice.spigot.helper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class TabCompleteUtil {

    private TabCompleteUtil() {
    }

    public static List<String> findEligibleCompletions(@Nonnull final String buffer, @Nonnull final String... options) {
        return Arrays.asList(options)
                .stream()
                .filter(option -> option.toLowerCase().startsWith(buffer.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<String> findEligiblePlayerNameCompletions(@Nonnull final String buffer) {
        return findEligibleCompletions(buffer,
                Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new));
    }
}
