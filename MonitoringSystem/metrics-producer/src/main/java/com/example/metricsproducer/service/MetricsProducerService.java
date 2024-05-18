package com.example.metricsproducer.service;

import com.example.metricsproducer.util.ResponseBodyMapper;
import com.example.model.MetricEvent;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class MetricsProducerService {

    @Value("${app.base-url}")
    private String baseUrl;
    @Value("${app.kafka.kafkaMessageTopic}")
    private String topicName;

    private final OkHttpClient okHttpClient;
    private final MetricsEndpoint metricsEndpoint;
    private final KafkaTemplate<String, List<MetricEvent>> kafkaTemplate;

    public void sendMetrics() {
        CompletableFuture.runAsync(() ->
                kafkaTemplate.send(topicName, getAllMetrics()));
    }

    private List<MetricEvent> getAllMetrics() {
        return getMetricNames().stream().map(this::getSingleMetric).toList();
    }

    private Set<String> getMetricNames() {
        return metricsEndpoint.listNames().getNames();
    }

    private MetricEvent getSingleMetric(String metricName) {
        MetricEvent metricEvent = null;
        Request request = new Request.Builder()
                .url(baseUrl.concat(metricName))
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            if (response.body() != null) {
                metricEvent = ResponseBodyMapper.parseJson(response.body().string(), MetricEvent.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return metricEvent;
    }
}
