package io.github.xtt28.identityservice.spigot.database.query;

import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.mysql.cj.jdbc.MysqlDataSource;

import io.github.xtt28.identityservice.spigot.database.dto.StudentDTO;

public final class StudentLookup {

    private StudentLookup() {

    }

    public static StudentDTO getStudentByPlayerUuid(@Nonnull final MysqlDataSource dataSource, @Nonnull final UUID uuid)
            throws SQLException {
        try (final var conn = dataSource.getConnection();
                final var stmt = conn.prepareStatement(
                        "SELECT * FROM `students` WHERE `id` IN (SELECT `student_id` FROM `registrations` WHERE `player_uuid` = ?);")) {
            stmt.setString(1, uuid.toString());
            final var resultSet = stmt.executeQuery();
            if (resultSet.next())
                return new StudentDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("academy"));

            throw new SQLException("Record not found.");
        }
    }
}
