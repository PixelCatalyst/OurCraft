package com.pixcat.voxel;

import com.pixcat.core.FileManager;
import com.pixcat.graphics.Texture;
import com.pixcat.graphics.Window;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class AnimatedBlockTest {
    private Window placeholderWindow;
    private Texture firstFrame;

    @Before
    public void setUp() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        placeholderWindow = new Window(100, 100, "placeholder");
        placeholderWindow.bindAsCurrent();
        firstFrame = FileManager.getInstance().loadTexture("water.png");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithZeroID() {
        new AnimatedBlock((byte) 0, "name", firstFrame, 1.0);
    }

    @Test
    public void testCreationWithNullName() {
        new AnimatedBlock((byte) 1, null, firstFrame, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationWithNullTexture() {
        new AnimatedBlock((byte) 1, "name", null, 1.0);
    }
    // Should be some kind of block for secondsPerFrame <= 0
    @Test
    public void testCreationWithZeroSecondsPerFrame() {
        new AnimatedBlock((byte) 1, "name", firstFrame, 0.0);
    }

    @Test
    public void testCreationWithNegativeSecondsPerFrame() {
        new AnimatedBlock((byte) 1, "name", firstFrame, -1.0);
    }


    @Test
    public void testValidCreation() {
        AnimatedBlock testBlock = new AnimatedBlock((byte) 1, "name", firstFrame, 1.0);
        assertEquals((byte) 1, testBlock.getID());
        assertEquals("name", testBlock.getName());
        assertEquals(firstFrame, testBlock.getTexture());
        assertTrue(testBlock.getFrameArray().get(0) == firstFrame);
        assertTrue(testBlock.getAccumulatedTime() == 0.0);
        assertTrue(testBlock.getSecondsPerFrame() == 1.0);
        assertTrue(testBlock.getCurrentFrameIndex() == 0);
    }

    @Test
    public void testUpdate(){
        AnimatedBlock testBlock1 = new AnimatedBlock((byte) 1, "name", firstFrame, 1.0);
        AnimatedBlock testBlock2 = new AnimatedBlock((byte) 1, "name", firstFrame, 1.0);
        testBlock1.update(1.0);
        assertEquals(testBlock1.getTexture(), testBlock2.getTexture()); //not working (not addding more frames)
        assertTrue(testBlock1.getAccumulatedTime() == 0.0);
        assertTrue(testBlock1.getCurrentFrameIndex() == 1); //same as above
    }

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(AnimatedBlock.class).verify();
    }


    @After
    public void tearDown() {
        placeholderWindow.destroy();
    }
}
