package org.example.readingservice.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    @Bean
    public MeterBinder meterBinder() {
        return registry -> {
            Counter.builder("total_readings_counter")
                .description("Total count of readings created")
                .register(registry);

            Counter.builder("readings_counter_by_type")
                    .description("Counts of readings by type")
                    .tag("type", "ГОРЯЧАЯ ВОДА")
                    .register(registry);
        };
    }
}
