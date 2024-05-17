package com.example.metricsconsumer.service;

import com.example.metricsconsumer.model.entity.Metric;

import java.util.List;

public interface MetricConsumerService {

    Metric getById(long id);
    List<Metric> getAllMetrics();
    void saveMetrics(List<Metric> metrics);
}
