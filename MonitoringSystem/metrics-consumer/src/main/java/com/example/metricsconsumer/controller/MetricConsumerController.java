package com.example.metricsconsumer.controller;

import com.example.metricsconsumer.model.dto.MetricResponse;
import com.example.metricsconsumer.model.entity.Metric;
import com.example.metricsconsumer.service.MetricConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MetricConsumerController {

    private final MetricConsumerService metricConsumerService;

    @GetMapping("/metrics/{id}")
    public ResponseEntity<MetricResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(MetricResponse.of(metricConsumerService.getById(id)));
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<MetricResponse>> getAllMetrics() {
        List<Metric> metrics = metricConsumerService.getAllMetrics();
        return ResponseEntity.ok(metrics.stream().map(MetricResponse::of).toList());
    }
}
