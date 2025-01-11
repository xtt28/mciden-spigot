package io.github.xtt28.identityservice.spigot.database.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.mysql.cj.jdbc.MysqlDataSource;

import io.github.xtt28.identityservice.spigot.database.dto.StudentDTO;

public final class StudentLookup {

    private StudentLookup() {

    }

    private static StudentDTO createStudentDtoFromResultSet(@Nonnull final ResultSet resultSet) throws SQLException {
        return new StudentDTO(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("academy"));
    }

    public static StudentDTO getStudentByPlayerUuid(@Nonnull final MysqlDataSource dataSource, @Nonnull final UUID uuid)
            throws SQLException {
        try (final var conn = dataSource.getConnection();
                final var stmt = conn.prepareStatement(
                        "SELECT * FROM `students` WHERE `id` IN (SELECT `student_id` FROM `registrations` WHERE `player_uuid` = ?);")) {
            stmt.setString(1, uuid.toString());
            final var resultSet = stmt.executeQuery();
            if (resultSet.next())
                return createStudentDtoFromResultSet(resultSet);

            throw new SQLException("Record not found.");
        }
    }

    public static StudentDTO getStudentByEmail(@Nonnull final MysqlDataSource dataSource, @Nonnull final String email)
            throws SQLException {
        try (final var conn = dataSource.getConnection();
                final var stmt = conn.prepareStatement(
                        "SELECT * FROM `students` WHERE `email` = ?;")) {
            stmt.setString(1, email);
            final var resultSet = stmt.executeQuery();
            if (resultSet.next())
                return createStudentDtoFromResultSet(resultSet);

            throw new SQLException("Record not found.");
        }
    }
}
