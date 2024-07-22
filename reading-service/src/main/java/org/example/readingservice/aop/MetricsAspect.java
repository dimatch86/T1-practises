package org.example.readingservice.aop;

import io.micrometer.core.instrument.Counter;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.readingservice.model.reading.Reading;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class MetricsAspect {
    private static final String HOT_WATER_READING_TYPE = "ГОРЯЧАЯ ВОДА";
    private  final Counter hotWaterReadingsCounter;

    @Pointcut("execution(* org.example.readingservice.service.ReadingServiceImpl.send(*))")
    public void sendReadingPointcut() {}

    @AfterReturning("args(reading) && sendReadingPointcut()")
    public void sendReadingAdvice(Reading reading){
        if (reading.getReadingType().equals(HOT_WATER_READING_TYPE)) {
            hotWaterReadingsCounter.increment();
        }
    }
}
