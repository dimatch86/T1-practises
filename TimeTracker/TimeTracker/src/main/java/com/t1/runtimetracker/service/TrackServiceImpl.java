package com.t1.runtimetracker.service;

import com.t1.runtimetracker.exception.ProceedingException;
import com.t1.runtimetracker.model.Track;
import com.t1.runtimetracker.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackServiceImpl implements TrackService {
    private final TrackRepository trackRepository;
    @Override
    public Object proceedAndSaveTrack(ProceedingJoinPoint joinPoint, boolean asynchronous) {
        long start;
        String methodName = joinPoint.getSignature().getName();
        Object result;
        if (asynchronous) {
            log.info("Выполнение асинхронного метода {}", methodName);
            start = System.currentTimeMillis();
            result = getAsyncResult(joinPoint);
        } else {
            log.info("Выполнение синхронного метода {}", methodName);
            start = System.currentTimeMillis();
            result = getSyncResult(joinPoint);
        }
        long duration = System.currentTimeMillis() - start;
        log.info("Метод {} выполнился за {} мс с результатом {}", methodName, duration, result);
        saveTrack(createTrack(methodName, duration));
        return result;
    }

    @SuppressWarnings("unchecked")
    private Object getAsyncResult(ProceedingJoinPoint joinPoint) {
        CompletableFuture<Object> asyncResult;
        try {
            Object result = joinPoint.proceed();
            if (result instanceof CompletableFuture<?>) {
                asyncResult = (CompletableFuture<Object>) joinPoint.proceed();
                return asyncResult.get();
            }
        } catch (Throwable e) {
            Thread.currentThread().interrupt();
            throw new ProceedingException(e.getMessage());
        }
        return null;
    }
    private Object getSyncResult(ProceedingJoinPoint joinPoint) {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new ProceedingException(e.getMessage());
        }
        return result;
    }

    private Track createTrack(String methodName, Long duration) {
        return Track.builder()
                .methodName(methodName)
                .duration(duration)
                .build();
    }

    private void saveTrack(Track track) {
        trackRepository.save(track);
    }
}
