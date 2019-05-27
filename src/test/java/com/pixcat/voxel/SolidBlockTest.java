package com.pixcat.voxel;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class SolidBlockTest {
    private Window placeholderWindow;
    private Texture testTexture;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(100, 100, "placeholder");
        placeholderWindow.bindAsCurrent();
        testTexture = FileManager.getInstance().loadTexture("dirt.png");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithZeroID() {
        new SolidBlock((byte) 0, "name", testTexture);
    }

    @Test
    public void testCreationWithNullName() {
        new SolidBlock((byte) 1, null, testTexture);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithNullTexture() {
        new SolidBlock((byte) 1, "name", null);
    }

    @Test
    public void testValidCreation() {
        Block testBlock = new SolidBlock((byte) 1, "name", testTexture);

        assertEquals((byte) 1, testBlock.getID());
        assertEquals("name", testBlock.getName());
        assertEquals(testTexture, testBlock.getTexture());
    }

    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }
}
