package com.t1.runtimetracker.service;

import com.t1.runtimetracker.dto.StatisticResponse;

import java.util.List;

public interface StatisticService {

    StatisticResponse getAverageDurationOfMethod(String methodName);
    StatisticResponse getMethodWithMaxDuration();
    StatisticResponse getAverageOfMethodsGroup(String groupPrefix);
    List<StatisticResponse> getMaxDurationsOfMethods();
    List<StatisticResponse> getMethodsWithDurationGreaterThan(Integer duration);


}
