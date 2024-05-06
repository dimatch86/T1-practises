package com.t1.runtimetracker.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ThreadUtils {

    public void waitRandomTime() {
        try {
            Thread.sleep((long) (Math.random() * 5000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
