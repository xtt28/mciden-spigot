package io.github.xtt28.identityservice.spigot.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import io.github.xtt28.identityservice.spigot.database.cache.ProfileCache;
import io.github.xtt28.identityservice.spigot.helper.ColorUtil;

public final class WhoAmICommand implements TabExecutor {

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.translate("&cYou're the console! |:-)"));
            return true;
        }

        final var player = (Player) sender;
        final var studentDto = ProfileCache.getStudentByPlayer(player.getUniqueId());

        final var msgLines = Arrays.asList(
            "&aFull name: &f" + studentDto.firstName() + " " + studentDto.lastName(),
            "&aAcademy: &f" + studentDto.academy(),
            "&aEmail: &f" + studentDto.email()
        ).stream().map(ColorUtil::translate).toArray(String[]::new);

        player.sendMessage(msgLines);
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }

}
