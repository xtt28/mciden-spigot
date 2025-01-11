package io.github.xtt28.identityservice.spigot.database.mutation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.mysql.cj.jdbc.MysqlDataSource;

import io.github.xtt28.identityservice.spigot.database.dto.StudentDTO;

public final class VerifyIntentCreate {

    private VerifyIntentCreate() {

    }

    public static String createVerifyIntent(@Nonnull final MysqlDataSource dataSource, @Nonnull final StudentDTO student,
            @Nonnull final UUID uuid)
            throws SQLException {
        try (final var conn = dataSource.getConnection();
                final var stmt = conn.prepareStatement(
                        "INSERT INTO `verify_intents` (`id`, `student_id`, `player_uuid`, `expires_at`) VALUES (?, ?, ?, ?);")) {

            final var expiry = Timestamp.valueOf(LocalDateTime.now().plusHours(1));

            stmt.setString(1, uuid.toString());
            stmt.setInt(2, student.id());
            stmt.setString(3, uuid.toString());
            stmt.setTimestamp(4, expiry);

            if (stmt.executeUpdate() == 0) // No rows affected
                throw new SQLException("Could not create verify intent - no rows affected.");

            return uuid.toString();
        }
    }
}
