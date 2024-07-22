package org.example.readingservice.aop;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.readingservice.model.reading.Reading;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
@RequiredArgsConstructor
public class MetricsAspect {
    private final MeterRegistry meterRegistry;

    @Pointcut("execution(* org.example.readingservice.service.ReadingServiceImpl.send(*))")
    public void sendReadingPointcut() {}

    @AfterReturning("args(reading) && sendReadingPointcut()")
    public void sendReadingAdvice(Reading reading) {

        meterRegistry.counter("total_readings_counter", List.of())
                .increment();

        meterRegistry.counter("readings_counter_by_type",
                List.of(Tag.of("type", reading.getReadingType())))
                .increment();
    }
}
