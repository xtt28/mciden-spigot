package io.github.xtt28.identityservice.spigot.command;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;
import javax.mail.Session;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.cj.jdbc.MysqlDataSource;

import io.github.xtt28.identityservice.spigot.database.dto.StudentDTO;
import io.github.xtt28.identityservice.spigot.database.mutation.VerifyIntentCreate;
import io.github.xtt28.identityservice.spigot.database.query.StudentLookup;
import io.github.xtt28.identityservice.spigot.email.MailSender;
import io.github.xtt28.identityservice.spigot.helper.ColorUtil;

public class VerifyCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final MysqlDataSource dataSource;
    private final Session session;
    private final String emailAddr;
    private final String urlTemplate;

    public VerifyCommand(@Nonnull final JavaPlugin plugin, @Nonnull final MysqlDataSource dataSource,
            @Nonnull final Session session,
            @Nonnull final String emailAddr, @Nonnull final String urlTemplate) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.session = session;
        this.emailAddr = emailAddr;
        this.urlTemplate = urlTemplate;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.translate("&cThis command must be executed as a player."));
            return true;
        }

        if (args.length != 1)
            return false;

        final var player = (Player) sender;
        StudentDTO studentDto;
        try {
            studentDto = StudentLookup.getStudentByEmail(this.dataSource, args[0]);
        } catch (SQLException e) {
            if (e.getMessage().equals("Record not found."))
                player.kickPlayer(ColorUtil.translate("&cYour email is not authorized to play on this server."));
            else
                player.kickPlayer(
                        ColorUtil.translate("&cCould not look up email data. This incident has been logged."));

            e.printStackTrace();
            return true;
        }

        final String id;
        try {
            id = VerifyIntentCreate.createVerifyIntent(dataSource, studentDto, UUID.randomUUID(), player.getUniqueId());
        } catch (SQLException ex) {
            player.kickPlayer(ColorUtil.translate(
                    "&cCould not create verification intent. Try again or contact support if the error persists."));
            ex.printStackTrace();
            return true;
        }

        try {
            MailSender.sendVerificationEmail(this.plugin, this.session, this.emailAddr, id, studentDto, this.urlTemplate);
            player.kickPlayer(ColorUtil.translate(
                    "&aPlease check your email for a verification link (you might have to check your spam folder)."));
        } catch (MessagingException | UnsupportedEncodingException ex) {
            player.kickPlayer(ColorUtil.translate(
                    "&cAn internal error occurred while sending email. Try again or contact support if the error persists."));
            ex.printStackTrace();
        }

        return true;
    }
}
