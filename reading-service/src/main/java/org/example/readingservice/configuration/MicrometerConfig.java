package org.example.readingservice.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    @Bean
    public Counter hotWaterReadingsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("hot_water_readings_counter")
                .tags("status", "created")
                .description("Total count of hot water readings created")
                .register(meterRegistry);
    }

    @Bean
    public Counter totalReadingsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("total_readings_counter")
                .tags("status", "created")
                .description("Total count of readings created")
                .register(meterRegistry);
    }
}
