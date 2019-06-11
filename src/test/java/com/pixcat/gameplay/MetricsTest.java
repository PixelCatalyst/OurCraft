package com.pixcat.gameplay;

import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MetricsTest {
    private Metrics testMetrics;

    @Before
    public void setUp() {
        testMetrics = new Metrics(0.0, 0, 0.0f);
    }

    @Test
    public void testDefaultCreation() {
        testMetrics = new Metrics();

        assertEquals(0.0, testMetrics.getSecondsInGame(), Double.MIN_NORMAL);
        assertEquals(0, testMetrics.getDirtBlocksDug());
        assertEquals(0.0f, testMetrics.getBlocksWalked(), Float.MIN_NORMAL);
        assertTrue(testMetrics.isInitialState());
    }

    @Test
    public void testCreationAndCopy() {
        Metrics metrics = new Metrics(15.0, 23, 13.0f);

        assertEquals(15.0, metrics.getSecondsInGame(), Double.MIN_NORMAL);
        assertEquals(23, metrics.getDirtBlocksDug());
        assertEquals(13.0f, metrics.getBlocksWalked(), Float.MIN_NORMAL);
        assertTrue(metrics.isInitialState());

        assertEquals(0.0, testMetrics.getSecondsInGame(), Double.MIN_NORMAL);
        assertEquals(0, testMetrics.getDirtBlocksDug());
        assertEquals(0.0f, testMetrics.getBlocksWalked(), Float.MIN_NORMAL);
        assertTrue(testMetrics.isInitialState());

        testMetrics = new Metrics(metrics);

        assertEquals(15.0, testMetrics.getSecondsInGame(), Double.MIN_NORMAL);
        assertEquals(23, testMetrics.getDirtBlocksDug());
        assertEquals(13.0f, testMetrics.getBlocksWalked(), Float.MIN_NORMAL);
        assertTrue(testMetrics.isInitialState());
    }

    @Test
    public void testAddSecondsInGame() {
        assertTrue(testMetrics.isInitialState());

        testMetrics.addSecondsInGame(1.0);
        testMetrics.addSecondsInGame(5.0);
        testMetrics.addSecondsInGame(13.0);
        testMetrics.addSecondsInGame(-13.0);
        testMetrics.addSecondsInGame(0.0);

        assertEquals(19.0, testMetrics.getSecondsInGame(), Double.MIN_NORMAL);
        assertFalse(testMetrics.isInitialState());
    }

    @Test
    public void testAddDugBlock() {
        assertTrue(testMetrics.isInitialState());

        //Metrics should only be interested in dirt of ID=1
        testMetrics.addDugBlock((byte) 1);
        testMetrics.addDugBlock((byte) 1);
        testMetrics.addDugBlock((byte) 0);
        testMetrics.addDugBlock((byte) -1);
        testMetrics.addDugBlock((byte) 11);

        assertEquals(2, testMetrics.getDirtBlocksDug());
        assertFalse(testMetrics.isInitialState());
    }

    @Test
    public void testAddPositionChange() {
        assertTrue(testMetrics.isInitialState());

        testMetrics.addPositionChange(new Vector3f(1.0f, 0.0f, 0.0f));
        testMetrics.addPositionChange(new Vector3f(-1.0f, 0.0f, 0.0f));
        testMetrics.addPositionChange(new Vector3f(1.0f, 0.0f, 0.0f));
        testMetrics.addPositionChange(new Vector3f(-1.0f, 0.0f, 0.0f));
        testMetrics.addPositionChange(new Vector3f(.0f, 1.0f, 0.0f)); //upward dir shouldn't be counted
        testMetrics.addPositionChange(new Vector3f(.0f, -10.0f, 0.0f));
        testMetrics.addPositionChange(new Vector3f(.0f, 0.0f, 0.0f));
        testMetrics.addPositionChange(new Vector3f(.0f, 0.0f, 1.0f));

        assertEquals(5.0f, testMetrics.getBlocksWalked(), Float.MIN_NORMAL);
        assertFalse(testMetrics.isInitialState());
    }
}
