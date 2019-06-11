package com.pixcat.graphics;

import org.junit.After;
import org.junit.Test;
import org.lwjgl.glfw.GLFWVidMode;

import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.*;

public class WindowTest {

    private Window window;

    @After
    public void destroyWindow() {
        window.destroy();
    }

    @Test
    public void testGetHeightWidth() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        window = new Window(500, 500, "window");
        window.bindAsCurrent();
        assertEquals(window.getHeight(), 500);
        assertEquals(window.getWidth(), 500 * window.getAspectRatio(), 0.0001);
    }

    @Test
    public void testAspectRatio() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        window = new Window(160, 100, "window");
        window.bindAsCurrent();
        assertEquals(window.getAspectRatio(), 1.6, 0.0001);
    }

    @Test
    public void testCenter() {
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        window = new Window(100, 100, "window");
        window.bindAsCurrent();
        int[] windowXOld = new int[1];
        int[] windowYOld = new int[1];
        glfwGetWindowPos(window.getHandle(), windowXOld, windowYOld);
        window.center();
        int[] windowXNew = new int[1];
        int[] windowYNew = new int[1];
        glfwGetWindowPos(window.getHandle(), windowXNew, windowYNew);
        assertNotEquals(windowXOld[0], windowXNew);
        assertNotEquals(windowYOld[0], windowYNew);
    }

    @Test
    public void testValidCreation() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        window = new Window(200, 100, "window");
        window.bindAsCurrent();
        assertTrue(window.isOpen());
        assertTrue(window.getHandle() != 0);
        assertTrue(window.getHeight() == 100);
        assertTrue((float) window.getWidth() == (window.getHeight() * window.getAspectRatio()));
    }

    @Test
    public void testActiveWindow() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        window = new Window(100, 100, "window");
        long x = glfwGetCurrentContext();
        assertEquals(x, 0);
        window.bindAsCurrent();
        long y = glfwGetCurrentContext();
        assertNotEquals(x, y);
    }
}
