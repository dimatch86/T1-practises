package com.example.metricsconsumer.repository;

import com.example.metricsconsumer.model.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    Optional<Metric> findByName(String name);
}
