package com.example.metricsconsumer.listener;

import com.example.metricsconsumer.model.entity.AvailableTag;
import com.example.metricsconsumer.model.entity.Measurement;
import com.example.metricsconsumer.model.entity.Metric;
import com.example.metricsconsumer.service.MetricConsumerServiceImpl;
import com.example.model.MetricEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MetricsListener {
    private final MetricConsumerServiceImpl metricConsumerServiceImpl;

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerOrderContainerFactory")
    public void listen(@Payload List<MetricEvent> metricEvents,
                       @Header(value = KafkaHeaders.KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(value = KafkaHeaders.PARTITION, required = false) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        metricConsumerServiceImpl.saveMetrics(metricEventsToMetrics(metricEvents));
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key, partition, topic, timestamp);
    }

    private List<Metric> metricEventsToMetrics(List<MetricEvent> metricEvents) {

        return metricEvents.stream().map(metricEvent -> Metric.builder()
                .name(metricEvent.getName())
                .description(metricEvent.getDescription())
                .baseUnit(metricEvent.getBaseUnit())
                .measurements(metricEvent.getMeasurements().stream().map(Measurement::measurementOf).toList())
                .availableTags(metricEvent.getAvailableTags().stream().map(AvailableTag::availableTagOf).toList())
                        .build())
                .toList();
    }
}
