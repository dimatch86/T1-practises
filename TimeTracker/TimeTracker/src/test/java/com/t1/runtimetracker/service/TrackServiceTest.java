package com.t1.runtimetracker.service;

import com.t1.runtimetracker.model.Track;
import com.t1.runtimetracker.repository.TrackRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrackServiceTest {
    private final TrackRepository trackRepository = mock(TrackRepository.class);
    private final TrackService trackService = new TrackServiceImpl(trackRepository);
    private final ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
    private final Signature signatureMock = mock(Signature.class);

    @Test
    void testProceedAndSaveTrackAsynchronous() throws Throwable {


        when(joinPoint.getSignature()).thenReturn(signatureMock);
        when(joinPoint.getSignature().getName()).thenReturn("testMethod");

        boolean asynchronous = true;
        String futureString = "asynchronousResult";
        CompletableFuture<Object> asyncResult = CompletableFuture.completedFuture(futureString);
        when(joinPoint.proceed()).thenReturn(asyncResult);

        trackService.proceedAndSaveTrack(joinPoint, asynchronous);
        String finalResult = (String) asyncResult.get();

        verify(trackRepository, times(1)).save(any(Track.class));
        assertEquals("asynchronousResult", finalResult);
    }
    @Test
    void testProceedAndSaveTrackSynchronous() throws Throwable {
        boolean asynchronous = false;

        Object expectedResult = new Object();
        when(joinPoint.getSignature()).thenReturn(signatureMock);

        when(joinPoint.getSignature().getName()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenReturn(expectedResult);

        Object result = trackService.proceedAndSaveTrack(joinPoint, asynchronous);

        assertEquals(expectedResult, result);
        verify(trackRepository, times(1)).save(any(Track.class));
    }
}
