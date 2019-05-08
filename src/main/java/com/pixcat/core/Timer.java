package com.pixcat.core;

public class Timer {
    private long prevTime;
    private boolean started = false;

    public double getElapsedSeconds() {
        long currentTime = System.nanoTime();
        double elapsed = 0.0;
        if (started)
            elapsed = toSeconds(currentTime - prevTime);
        else
            started = true;
        prevTime = currentTime;
        return elapsed;
    }

    private double toSeconds(long nanoseconds) {
        return nanoseconds / 1e9;
    }
}
