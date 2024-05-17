package com.example.metricsproducer.controller;

import com.example.metricsproducer.service.MetricsProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MetricsProducerController {
    private final MetricsProducerService metricsProducerService;

    @PostMapping("/metrics")
    public ResponseEntity<String> sendMessage() {
        metricsProducerService.sendMetrics();
        return ResponseEntity.ok("Message send");
    }
}
