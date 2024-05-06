package com.t1.runtimetracker.repository;

import com.t1.runtimetracker.dto.StatisticResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticRepositoryImpl implements StatisticRepository {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public StatisticResponse getAverageDuration(String methodName) {
        String sql = "SELECT method_name, AVG(duration) as duration " +
                "FROM time_track " +
                "WHERE method_name = ? " +
                "GROUP BY method_name";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, methodName);
        return mapSingleResult(rowSet);
    }
    @Override
    public StatisticResponse getAverageDurationOfGroup(String groupPrefix) {
        String methodNamePattern = groupPrefix + "%";
        String sql = "SELECT AVG(avg_duration) as duration  " +
                "FROM (SELECT method_name, AVG(duration) as avg_duration " +
                "FROM time_track " +
                "GROUP BY method_name) " +
                "WHERE method_name like ?";
        Double methodGroupAverageDuration = jdbcTemplate.queryForObject(sql, Double.class, methodNamePattern);
        return StatisticResponse.builder()
                .methodGroupName(groupPrefix)
                .duration(methodGroupAverageDuration).build();
    }
    @Override
    public StatisticResponse getMethodWithMaxDuration() {
        String sql = "SELECT method_name, duration " +
                "FROM time_track " +
                "WHERE duration = (SELECT MAX(duration) FROM time_track)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        return mapSingleResult(rowSet);
    }
    @Override
    public List<StatisticResponse> getMethodsWithMaxDurations() {
        String sql = "SELECT method_name, MAX(duration) as duration " +
                "FROM time_track " +
                "GROUP BY method_name";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        return mapListOfResults(rowSet);
    }

    @Override
    public List<StatisticResponse> getMethodsWithDurationGreaterThan(Integer duration) {
        String sql = "SELECT method_name, duration " +
                "FROM time_track " +
                "WHERE duration > ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, duration);
        return mapListOfResults(rowSet);
    }


    private StatisticResponse mapSingleResult(SqlRowSet rowSet) {
        StatisticResponse statisticResponse = null;
        if (rowSet.next()) {
            statisticResponse = StatisticResponse.builder()
                    .methodName(rowSet.getString("method_name"))
                    .duration(rowSet.getDouble("duration"))
                    .build();
        }
        return statisticResponse;
    }
    private List<StatisticResponse> mapListOfResults(SqlRowSet rowSet) {
        List<StatisticResponse> statisticResponses = new ArrayList<>();
        while (rowSet.next()) {
            StatisticResponse response = StatisticResponse.builder()
                    .methodName(rowSet.getString("method_name"))
                    .duration(rowSet.getDouble("duration"))
            .build();
            statisticResponses.add(response);
        }
        return statisticResponses;
    }
}
