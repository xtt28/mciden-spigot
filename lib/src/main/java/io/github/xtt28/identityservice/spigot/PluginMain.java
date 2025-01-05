package io.github.xtt28.identityservice.spigot;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

import io.github.xtt28.identityservice.spigot.listener.PlayerJoinListener;

public final class PluginMain extends JavaPlugin {

    private final MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();

    private final void setupDatabase() {
        dataSource.setServerName(this.getConfig().getString("db.host"));
        dataSource.setPortNumber(this.getConfig().getInt("db.port"));
        dataSource.setDatabaseName(this.getConfig().getString("db.db"));
        dataSource.setUser(this.getConfig().getString("db.user"));
        dataSource.setPassword(this.getConfig().getString("db.password"));
    }

    private final void testDatabaseConnection() throws SQLException {
        try (var conn = dataSource.getConnection()) {
            if (!conn.isValid(1))
                throw new SQLException("Could not connect to database.");
        }
    }

    private final void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this.getLogger(), this.dataSource), this);
    }

    @Override
    public final void onEnable() {
        this.getLogger().info("Beginning to establish connection to MySQL database.");
        this.setupDatabase();
        try {
            this.getLogger().info("Testing connection to MySQL database.");
            this.testDatabaseConnection();
        } catch (final SQLException ex) {
            this.getLogger().severe("Couldn't establish database connection. Disabling plugin.");
            ex.printStackTrace();
        }

        this.registerListeners();
    }
}
