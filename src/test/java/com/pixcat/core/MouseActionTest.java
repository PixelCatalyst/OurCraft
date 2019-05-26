package com.pixcat.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class MouseActionTest {

    @Test
    public void testEmptyInitialization() {
        MouseAction emptyAction = new MouseAction();

        assertEquals(MouseAction.Button.NONE, emptyAction.getButton());
        assertEquals(MouseAction.Event.NONE, emptyAction.getEvent());
        assertTrue(emptyAction.getX() < 0.0);
        assertTrue(emptyAction.getY() < 0.0);
    }

    @Test
    public void testTypicalInitialization() {
        MouseAction typicalAction = new MouseAction(MouseAction.Button.LEFT, MouseAction.Event.PRESS, 31.0, 43.0);

        assertEquals(MouseAction.Button.LEFT, typicalAction.getButton());
        assertEquals(MouseAction.Event.PRESS, typicalAction.getEvent());
        assertEquals(31.0, typicalAction.getX(), 0.0);
        assertEquals(43.0, typicalAction.getY(), 0.0);
    }
}
