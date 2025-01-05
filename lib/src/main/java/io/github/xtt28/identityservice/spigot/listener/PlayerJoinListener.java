package io.github.xtt28.identityservice.spigot.listener;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import com.mysql.cj.jdbc.MysqlDataSource;

import io.github.xtt28.identityservice.spigot.database.cache.ProfileCache;
import io.github.xtt28.identityservice.spigot.database.query.StudentLookup;

public final class PlayerJoinListener implements Listener {

    private final Logger logger;
    private final MysqlDataSource dataSource;

    public PlayerJoinListener(@Nonnull final Logger logger, @Nonnull final MysqlDataSource dataSource) {
        this.logger = logger;
        this.dataSource = dataSource;
    }

    @EventHandler
    public final void onAsyncPlayerPreLogin(@Nonnull final AsyncPlayerPreLoginEvent e) {
        try {
            this.logger.log(Level.INFO,
                    MessageFormat.format("Looking up student data for {0} (UUID {1})", e.getName(), e.getUniqueId()));
            final var studentDto = StudentLookup.getStudentByPlayerUuid(this.dataSource, e.getUniqueId());

            ProfileCache.cachePlayer(e.getUniqueId(), studentDto);
        } catch (final SQLException ex) {
            if (ex.getMessage().equals("Record not found.")) {
                this.logger.log(Level.INFO,
                        MessageFormat.format("Kicking {0} (UUID {1}) because their account is not registered",
                                e.getName(), e.getUniqueId()));
                e.disallow(Result.KICK_WHITELIST, "§cYou aren't whitelisted on this server.");
            } else {
                this.logger.log(Level.INFO,
                        MessageFormat.format("Kicking {0} (UUID {1}) because we couldn't look up profile data",
                                e.getName(), e.getUniqueId()),
                        ex);
                e.disallow(Result.KICK_OTHER,
                        "§cCouldn't look up your data. If this error persists, please contact support.");
            }
        }
    }
}
