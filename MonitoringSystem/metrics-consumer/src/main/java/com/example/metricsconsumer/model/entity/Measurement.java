package com.example.metricsconsumer.model.entity;

import com.example.model.MeasurementEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "measurement")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statistic;
    private String value;

    public static Measurement measurementOf(MeasurementEvent event) {
        return Measurement.builder()
                .statistic(event.getStatistic())
                .value(event.getValue().toString())
                .build();
    }
}
