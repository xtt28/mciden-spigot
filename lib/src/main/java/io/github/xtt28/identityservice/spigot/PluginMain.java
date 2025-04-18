package io.github.xtt28.identityservice.spigot;

import java.sql.SQLException;

import javax.mail.Session;

import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

import io.github.xtt28.identityservice.spigot.command.VerifyCommand;
import io.github.xtt28.identityservice.spigot.command.WhoAmICommand;
import io.github.xtt28.identityservice.spigot.command.WhoIsCommand;
import io.github.xtt28.identityservice.spigot.email.SessionCreator;
import io.github.xtt28.identityservice.spigot.listener.PlayerJoinListener;
import io.github.xtt28.identityservice.spigot.placeholder.IdentityPlaceholderExpansion;

public final class PluginMain extends JavaPlugin {

    private final MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
    private Session mailSession;

    private final boolean isHubServer() {
        return this.getConfig().getBoolean("this-is-a-hub-server");
    }

    // Most database connection code is taken from:
    // https://www.spigotmc.org/wiki/connecting-to-databases-mysql/
    private final void setupDatabase() {
        dataSource.setServerName(this.getConfig().getString("db.host"));
        dataSource.setPortNumber(this.getConfig().getInt("db.port"));
        dataSource.setDatabaseName(this.getConfig().getString("db.db"));
        dataSource.setUser(this.getConfig().getString("db.user"));
        dataSource.setPassword(this.getConfig().getString("db.password"));
    }

    private final void setupEmail() {
        this.mailSession = SessionCreator.createSessionFromConfig(this.getConfig());
    }

    private final void testDatabaseConnection() throws SQLException {
        try (final var conn = dataSource.getConnection()) {
            if (!conn.isValid(1))
                throw new SQLException("Could not connect to database.");
        }
    }

    private final void registerListeners() {
        if (!this.isHubServer())
            this.getServer().getPluginManager().registerEvents(
                    new PlayerJoinListener(this.getLogger(), this.dataSource),
                    this);
    }

    private final void registerCommands() {
        if (this.isHubServer())
            this.getCommand("verify").setExecutor(
                    new VerifyCommand(this, this.dataSource, this.mailSession,
                            this.getConfig().getString("mail.address"),
                            this.getConfig().getString("verify.url-template")));
        else {
            this.getCommand("whoami").setExecutor(new WhoAmICommand());
            this.getCommand("whois").setExecutor(new WhoIsCommand());
        }
    }

    private final void registerPlaceholders() {
        final var isPapiPresent = this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        final var isHub = this.isHubServer();
        this.getLogger().info("PlaceholderAPI enabled: " + isPapiPresent);
        this.getLogger().info("Hub server: " + this.isHubServer());

        if (isPapiPresent && !isHub) {
            this.getLogger().info("Registering PlaceholderAPI expansion.");
            final var result = new IdentityPlaceholderExpansion(this.getDescription()).register();
            if (!result)
                this.getLogger().warning("Could not register PlaceholderAPI expansion.");
        }
    }

    @Override
    public final void onEnable() {
        this.saveDefaultConfig();

        this.getLogger().info("Beginning to establish connection to MySQL database.");
        this.setupDatabase();
        try {
            this.getLogger().info("Testing connection to MySQL database.");
            this.testDatabaseConnection();
        } catch (final SQLException ex) {
            this.getLogger().severe("Couldn't establish database connection. Disabling plugin.");
            ex.printStackTrace();
        }

        this.setupEmail();
        this.registerPlaceholders();
        this.registerListeners();
        this.registerCommands();
    }
}
