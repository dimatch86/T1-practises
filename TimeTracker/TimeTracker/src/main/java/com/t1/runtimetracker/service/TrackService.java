package com.t1.runtimetracker.service;

import org.aspectj.lang.ProceedingJoinPoint;

public interface TrackService {

    Object proceedAndSaveTrack(ProceedingJoinPoint joinPoint, boolean asynchronous);
}
