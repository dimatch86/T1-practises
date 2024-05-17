package com.example.metricsconsumer.model.dto;

import com.example.metricsconsumer.model.entity.AvailableTag;
import com.example.metricsconsumer.model.entity.Measurement;
import com.example.metricsconsumer.model.entity.Metric;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetricResponse {

    private String name;
    private String description;
    private String baseUnit;
    private List<Measurement> measurements;
    private List<AvailableTag> availableTags;

    public static MetricResponse of(Metric metric) {
        return MetricResponse.builder()
                .name(metric.getName())
                .description(metric.getDescription())
                .baseUnit(metric.getBaseUnit())
                .measurements(metric.getMeasurements())
                .availableTags(metric.getAvailableTags())
                .build();
    }
}
