package com.pixcat.core;

import com.pixcat.graphics.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_SPACE;
import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class InputBufferTest {
    private InputBuffer testInput;
    private Window placeholderWindow;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(200, 200, "placeholder");
        placeholderWindow.bindAsCurrent();

        testInput = new InputBuffer(placeholderWindow.getHandle());
    }

    @Test
    public void testIsKeyboardKeyDown() {
        try {
            Robot robot = new Robot();

            assertFalse(testInput.isKeyboardKeyDown(GLFW_KEY_SPACE));
            robot.keyPress(VK_SPACE);
            testInput.update();

            assertTrue(testInput.isKeyboardKeyDown(GLFW_KEY_SPACE));

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsAnyKeyboardKeyDown() {
        try {
            Robot robot = new Robot();

            assertFalse(testInput.isAnyKeyboardKeyDown());
            robot.keyPress(VK_SPACE);
            robot.keyPress(VK_ENTER);
            testInput.update();

            assertTrue(testInput.isAnyKeyboardKeyDown());

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetMouseLocked() {
        testInput.setMouseLocked(); //Shouldn't throw anything
    }

    @Test
    public void testSetMouseUnlocked() {
        testInput.setMouseUnlocked(); //Shouldn't throw anything
    }

    //Technically, getMouseX/Y value is always correct. More specific errors are verified by system tests
    @Test
    public void testGetMouseX() {
        testInput.getMouseX(); //Shouldn't throw anything
    }

    @Test
    public void testGetMouseY() {
        testInput.getMouseY(); //Shouldn't throw anything
    }

    //More specific errors are verified by system tests - application wouldn't work without MouseAction's
    @Test
    public void testGetMouseAction() {
        MouseAction actualAction = testInput.getMouseAction();
        MouseAction emptyAction = new MouseAction();

        assertEquals(emptyAction.getX(), actualAction.getX(), Double.MIN_NORMAL);
        assertEquals(emptyAction.getY(), actualAction.getY(), Double.MIN_NORMAL);
        assertEquals(emptyAction.getEvent(), actualAction.getEvent());
        assertEquals(emptyAction.getButton(), actualAction.getButton());
    }

    @Test
    public void testPendingMouseAction() {
        assertFalse(testInput.pendingMouseAction());
    }

    @Test
    public void testFlushMouseActions() {
        testInput.flushMouseActions();
        assertFalse(testInput.pendingMouseAction());
    }

    //Technically, scrollOffset value is always correct. More specific errors are verified by system tests
    @Test
    public void testGetScrollOffset() {
        testInput.getScrollOffset(); //Shouldn't throw anything
    }

    @Test
    public void testUpdate() {
        testInput.update(); //Shouldn't throw anything
    }

    @Test
    public void testClearKeyBuffer() {
        try {
            Robot robot = new Robot();
            testInput.clearKeyBuffer();

            assertFalse(testInput.isAnyKeyboardKeyDown());
            robot.keyPress(VK_SPACE);
            robot.keyPress(VK_ENTER);
            testInput.update();

            assertTrue(testInput.isAnyKeyboardKeyDown());
            testInput.clearKeyBuffer();
            assertFalse(testInput.isAnyKeyboardKeyDown());

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
        testInput.clearKeyBuffer();
    }
}
