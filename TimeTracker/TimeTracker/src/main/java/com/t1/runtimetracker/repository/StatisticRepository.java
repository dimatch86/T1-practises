package com.t1.runtimetracker.repository;

import com.t1.runtimetracker.dto.StatisticResponse;

import java.util.List;

public interface StatisticRepository {

    StatisticResponse getAverageDuration(String methodName);
    StatisticResponse getAverageDurationOfGroup(String groupPrefix);
    StatisticResponse getMethodWithMaxDuration();
    List<StatisticResponse> getMethodsWithMaxDurations();
    List<StatisticResponse> getMethodsWithDurationGreaterThan(Integer duration);


}
