package io.github.xtt28.identityservice.spigot.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import io.github.xtt28.identityservice.spigot.database.cache.ProfileCache;
import io.github.xtt28.identityservice.spigot.helper.ColorUtil;
import io.github.xtt28.identityservice.spigot.helper.TabCompleteUtil;

public final class WhoIsCommand implements TabExecutor {

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (args.length != 1) {
            return false;
        }

        final var player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(ColorUtil.translate("&cThis player is not online."));
            return true;
        }

        final var studentDto = ProfileCache.getStudentByPlayer(player.getUniqueId());

        final var msgLines = Arrays.asList(
            "&aFull name: &f" + studentDto.firstName() + " " + studentDto.lastName(),
            "&aAcademy: &f" + studentDto.academy(),
            "&aEmail: &f" + studentDto.email()
        ).stream().map(ColorUtil::translate).toArray(String[]::new);

        sender.sendMessage(msgLines);
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return switch (args.length) {
            case 1 -> TabCompleteUtil.findEligiblePlayerNameCompletions(args[0]);
            default -> List.of();
        };
    }

}
