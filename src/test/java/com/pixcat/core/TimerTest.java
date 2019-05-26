package com.pixcat.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimerTest {
    private Timer testTimer = new Timer();

    @Test
    public void testInitialTimeIsZero() {
        double actualSeconds = testTimer.getElapsedSeconds();

        assertEquals(0.0, actualSeconds, 0.0);
    }

    @Test
    public void testTimeSteps() {
        try {
            testTimer.getElapsedSeconds();
            Thread.sleep(1000);
            double afterSecond = testTimer.getElapsedSeconds();
            Thread.sleep(2000);
            double afterThreeSeconds = afterSecond + testTimer.getElapsedSeconds();

            final double tenMillisecondsMargin = 0.01;
            assertEquals(afterSecond, 1.0, tenMillisecondsMargin);
            assertEquals(afterThreeSeconds, 3.0, tenMillisecondsMargin);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt");
        }
    }
}
