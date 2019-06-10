package com.pixcat.graphics;

import com.pixcat.core.FileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL33.GL_TEXTURE_BINDING_2D;
import static org.lwjgl.opengl.GL33.glGetIntegerv;


public class TextureTest {
    Texture blockTexture;
    Window window;
    int blockSize = 16;

    @Before
    public void beforeEach() {
        if (glfwInit() == false)
            throw new RuntimeException("Unable to initialize GLFW");
        window = new Window(100, 100, "window");
        window.bindAsCurrent();

        FileManager fm = FileManager.getInstance();
        blockTexture = fm.loadTexture("dirt.png");
    }

    @After
    public void destroyWindow() {
        window.destroy();
    }

    @Test
    public void testBlockSize() {
        assertEquals(blockSize, blockTexture.getWidth());
        assertEquals(blockSize, blockTexture.getHeight());
    }

    @Test
    public void testTextureBinding() {
        blockTexture.bind();
        int[] texID = new int[1];
        glGetIntegerv(GL_TEXTURE_BINDING_2D, texID);
        assertEquals(texID[0], blockTexture.getID());
    }

    @Test
    public void testRefCount() {
        blockTexture.addReference();
        blockTexture.addReference();
        blockTexture.addReference();

        blockTexture.cleanup();
        assertNotEquals(0, blockTexture.getID());

        blockTexture.cleanup();
        assertNotEquals(0, blockTexture.getID());

        blockTexture.cleanup();
        int[] texID = new int[1];
        glGetIntegerv(GL_TEXTURE_BINDING_2D, texID);
        System.out.println(texID[0]);
        assertEquals(0, texID[0]);
        assertEquals(0, blockTexture.getID());
    }

    @Test
    public void testEqualsSameRef() {
        assertTrue(blockTexture.equals(blockTexture));
    }
}
