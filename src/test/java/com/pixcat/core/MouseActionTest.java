package com.pixcat.core;

import org.joml.Vector3f;
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

    @Test
    public void testTranslateCoords(){
        Vector3f vector = new Vector3f(5,5,5);
        MouseAction mouseAction = new MouseAction(MouseAction.Button.LEFT, MouseAction.Event.PRESS, 31.0, 43.0);
        mouseAction.translateCoords(vector);
        assertEquals(31.0-5, mouseAction.getX(), 0.0);
        assertEquals(43.0-5, mouseAction.getY(), 0.0);
    }
}
