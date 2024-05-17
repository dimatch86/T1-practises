package com.example.model;

import lombok.Data;

import java.util.List;

@Data
public class MetricEvent {
    private String name;
    private String description;
    private String baseUnit;
    private List<MeasurementEvent> measurements;
    private List<AvailableTagEvent> availableTags;

}
