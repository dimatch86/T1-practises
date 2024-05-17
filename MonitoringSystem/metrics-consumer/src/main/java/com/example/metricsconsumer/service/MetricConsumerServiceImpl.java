package com.example.metricsconsumer.service;

import com.example.metricsconsumer.exception.MetricNotFoundException;
import com.example.metricsconsumer.model.entity.Measurement;
import com.example.metricsconsumer.model.entity.Metric;
import com.example.metricsconsumer.repository.MetricRepository;
import com.example.metricsconsumer.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricConsumerServiceImpl implements MetricConsumerService {
    private final MetricRepository metricRepository;

    @Override
    public Metric getById(long id) {
        return metricRepository.findById(id)
                .orElseThrow(() -> new MetricNotFoundException("Metric not found"));
    }

    @Override
    public List<Metric> getAllMetrics() {
        return metricRepository.findAll();
    }

    @Override
    public void saveMetrics(List<Metric> metrics) {
        metrics.forEach(this::saveOrUpdateMetric);
    }

    private void saveOrUpdateMetric(Metric metric) {
        metricRepository.findByName(metric.getName())
                .ifPresentOrElse(existingMetric -> {

                    List<Measurement> updatedMeasurements =
                            existingMetric.getMeasurements().stream().peek(measurement -> {
                        Measurement newMeasurement = metric.getMeasurements().stream()
                                .filter(m -> m.getStatistic().equals(measurement.getStatistic()))
                                .findFirst().orElse(measurement);
                        BeanUtil.copyNonNullProperties(newMeasurement, measurement);
                    }).toList();

                    existingMetric.setMeasurements(updatedMeasurements);
                    metricRepository.save(existingMetric);
                },
                        () -> metricRepository.save(metric));
    }
}
