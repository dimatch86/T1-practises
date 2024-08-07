package org.example.readingservice.repository;

import lombok.RequiredArgsConstructor;
import org.example.readingservice.mapper.jdbc_mapper.ReadingRawMapper;
import org.example.readingservice.model.reading.Reading;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.*;

/**
 * Implementation of the ReadingRepository interface that interacts with a database.
 */
@RequiredArgsConstructor
@Repository
public class ReadingRepositoryImpl implements ReadingRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Saves a new reading to the database.
     * @param reading the reading to be saved
     */
    @Transactional
    public void save(Reading reading) {

        String sql = "INSERT INTO public.reading " +
                "(id, reading_value, personal_account, type_id, sending_date) " +
                "VALUES (NEXTVAL('jdbc_sequence'), ?, ?, ?, ?)";
        jdbcTemplate.update(sql, reading.getReadingValue(), String.valueOf(reading.getPersonalAccount()),
                getTypeIdByType(reading.getReadingType()), java.sql.Timestamp.from(reading.getSendingDate()));
    }

    /**
     * Retrieves the latest reading for a specific reading type from the database.
     * @param readingType the type of reading for which the latest reading should be retrieved
     * @return an optional containing the latest reading for the specified type, or empty if not found
     */
    public Optional<Reading> getLatestReading(String readingType, String personalAccount) {

        String latestReadingQuery = "SELECT * " +
                "FROM public.reading r " +
                "JOIN public.available_reading ar ON r.type_id = ar.id " +
                "WHERE r.type_id = " +
                "(SELECT id FROM public.available_reading arr WHERE arr.type = ?) " +
                "AND r.personal_account = ? " +
                "ORDER BY r.sending_date DESC LIMIT 1";
        return Optional.ofNullable(jdbcTemplate.query(latestReadingQuery,
                new ReadingRawMapper(readingType), readingType,
                personalAccount));
    }

    /**
     * Finds the actual readings for the users based on their roles.
     * @return A list of Reading objects representing the actual readings found
     */
    @Override
    public List<Reading> findActualReadings(String personalAccount) {

        String sql = "SELECT * FROM public.reading r JOIN" +
                "    ( SELECT type_id, MAX(sending_date) AS max_date" +
                "     FROM public.reading GROUP BY type_id, personal_account) max_dates" +
                "     ON r.type_id = max_dates.type_id AND r.sending_date = max_dates.max_date" +
                "     JOIN public.user u ON r.personal_account = u.personal_account" +
                "     WHERE  (SELECT u.role" +
                "            from public.user u" +
                "            where u.personal_account = ?) = 'ROLE_ADMIN'" +
                "     OR (SELECT u.role from public.user u" +
                "      where u.personal_account = ?) = 'ROLE_USER' AND u.personal_account = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, personalAccount, personalAccount, personalAccount);
        return extractReadingsFromResultSet(rowSet);
    }


    /**
     * Finds the readings for a specific month based on the month number and user role.
     * @param monthNumber The month number to filter the readings
     * @return A list of Reading objects representing the readings found for the specified month
     */
    @Override
    public List<Reading> findReadingsByMonth(int monthNumber, String personalAccount) {
        String sql ="SELECT * FROM public.reading r" +
                "    JOIN public.available_reading ar ON r.type_id = ar.id" +
                "    JOIN public.user u ON r.personal_account = u.personal_account" +
                "    WHERE  extract(MONTH FROM sending_date) = ? and (SELECT u.role" +
                "            from public.user u" +
                "                                          where u.personal_account = ?) = 'ROLE_ADMIN'" +
                "OR extract(MONTH FROM sending_date) = ? and (SELECT u.role from public.user u" +
                "     where u.personal_account = ?) = 'ROLE_USER' AND u.personal_account = ? " +
                "order by sending_date desc";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, monthNumber, personalAccount, monthNumber, personalAccount, personalAccount);
        return extractReadingsFromResultSet(rowSet);
    }

    /**
     * Finds the readings history based on the user role.
     * @return A list of Reading objects representing the readings history
     */
    @Override
    public List<Reading> findReadingsHistory(String personalAccount) {
        String sql = "SELECT * FROM public.reading r " +
                "     JOIN public.user u ON r.personal_account = u.personal_account " +
                "     WHERE  (SELECT u.role" +
                "            from public.user u" +
                "            where u.personal_account = ?) = 'ROLE_ADMIN'" +
                "     OR (SELECT u.role from public.user u" +
                "      where u.personal_account = ?) = 'ROLE_USER' AND u.personal_account = ? " +
                "     ORDER BY sending_date DESC ";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, personalAccount, personalAccount, personalAccount);
        return extractReadingsFromResultSet(rowSet);
    }


    /**
     * Retrieves the type based on the type ID.
     * @param typeId The ID of the type to find
     * @return A String representing the type
     */
    private String getTypeByTypeId(int typeId) {
        String sql = "SELECT ar.type FROM public.available_reading ar " +
                "WHERE ar.id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, typeId);
    }

    /**
     * Retrieves the type ID based on the given type.
     * @param type The type of the reading
     * @return An integer representing the type ID
     */
    private Integer getTypeIdByType(String type) {
        String sql = "SELECT id FROM public.available_reading ar" +
                " WHERE ar.type = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, type);
    }

    private List<Reading> extractReadingsFromResultSet(SqlRowSet rowSet) {
        List<Reading> readings = new ArrayList<>();
        while (rowSet.next()) {
            Reading reading = new Reading(
                    rowSet.getDouble("reading_value"),
                    UUID.fromString(Objects.requireNonNull(rowSet.getString("personal_account"))),
                    getTypeByTypeId(rowSet.getInt("type_id")),
                    rowSet.getDate("sending_date").toLocalDate()
                            .atStartOfDay(ZoneId.systemDefault()).toInstant());
            readings.add(reading);
        }
        return readings;
    }
}
