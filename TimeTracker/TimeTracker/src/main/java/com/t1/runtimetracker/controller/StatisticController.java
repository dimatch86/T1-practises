package com.t1.runtimetracker.controller;

import com.t1.runtimetracker.dto.StatisticResponse;
import com.t1.runtimetracker.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/avg")
    public ResponseEntity<StatisticResponse> getAverageDuration(@RequestParam(name = "name") String name) {
        return ResponseEntity.ok(statisticService.getAverageDurationOfMethod(name));
    }

    @GetMapping("/max")
    public ResponseEntity<StatisticResponse> getMaxDuration() {
        return ResponseEntity.ok(statisticService.getMethodWithMaxDuration());
    }

    @GetMapping("/avg-group")
    public ResponseEntity<StatisticResponse> getAverageOfGroup(@RequestParam(name = "group") String groupPrefix) {
        return ResponseEntity.ok(statisticService.getAverageOfMethodsGroup(groupPrefix));
    }

    @GetMapping("/maximums")
    public ResponseEntity<List<StatisticResponse>> getMaxDurations() {
        return ResponseEntity.ok(statisticService.getMaxDurationsOfMethods());
    }

    @GetMapping("/greater-than")
    public ResponseEntity<List<StatisticResponse>> getDurationsGreaterThan(@RequestParam(name = "duration") Integer duration) {
        return ResponseEntity.ok(statisticService.getMethodsWithDurationGreaterThan(duration));
    }
}
