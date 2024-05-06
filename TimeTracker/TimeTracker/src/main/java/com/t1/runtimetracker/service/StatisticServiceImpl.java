package com.t1.runtimetracker.service;

import com.t1.runtimetracker.dto.StatisticResponse;
import com.t1.runtimetracker.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;
    @Override
    public StatisticResponse getAverageDurationOfMethod(String methodName) {
        return statisticRepository.getAverageDuration(methodName);
    }

    @Override
    public StatisticResponse getMethodWithMaxDuration() {
        return statisticRepository.getMethodWithMaxDuration();
    }

    @Override
    public StatisticResponse getAverageOfMethodsGroup(String groupPrefix) {
        return statisticRepository.getAverageDurationOfGroup(groupPrefix);
    }

    @Override
    public List<StatisticResponse> getMaxDurationsOfMethods() {
        return statisticRepository.getMethodsWithMaxDurations();
    }

    @Override
    public List<StatisticResponse> getMethodsWithDurationGreaterThan(Integer duration) {
        return statisticRepository.getMethodsWithDurationGreaterThan(duration);
    }
}
